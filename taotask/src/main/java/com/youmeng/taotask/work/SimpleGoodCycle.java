package com.youmeng.taotask.work;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.youmeng.common.base.taotask.entity.Good;
import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.common.base.taotask.entity.Tasklog;
import com.youmeng.common.base.taotask.entity.User;
import com.youmeng.common.base.taotask.response.ResponseDealResultCode;
import com.youmeng.taotask.service.GoodService;
import com.youmeng.taotask.service.TaskService;
import com.youmeng.taotask.service.TasklogService;
import com.youmeng.taotask.service.WorkService;
import com.youmeng.taotask.serviceImpl.RedisService;
import com.youmeng.taotask.util.SpringContextUtils;

/**
 * 循环上下架
 * @author Administrator
 *
 */
public class SimpleGoodCycle {
	private GoodService goodService = SpringContextUtils.getBean(GoodService.class);
    private Logger logger = LoggerFactory.getLogger(getClass());
	private RedisService redis = SpringContextUtils.getBean(RedisService.class);;
    private StringRedisTemplate stringRedisTemplate = SpringContextUtils.getBean(StringRedisTemplate.class);;
    private TaskService taskService = SpringContextUtils.getBean(TaskService.class);
    private WorkService workService = SpringContextUtils.getBean(WorkService.class);
    private TasklogService tasklogService = SpringContextUtils.getBean(TasklogService.class);

    
    private String taskId;
    private Task task;
    private User user;
	private boolean flag;
	private BlockingQueue<Good> acquiredGoods;		//读取得的商品
	private int success = 0;
	
	private BlockingQueue<Integer> endTaskFlag = new LinkedBlockingQueue<Integer>();	//堵塞标记：等待其他的所有完成后，进行恢复商品
	private BlockingQueue<Good> recoverGoods = new LinkedBlockingQueue<Good>();				//需要恢复的商品
	private BlockingQueue<Good> dealGoods = new LinkedBlockingQueue<Good>();				//需要处理的商品（经过第一步加工之后的商品）
	
	private long startTime;
	private long currentPage;		//当前读取的商品页数
    public SimpleGoodCycle(String taskId){
    	this.taskId = taskId;
    }
    
    /**
     * 初始化任务，获取商品，并记录任务
     */
	public void init(){ 
		//redisTemplate.opsForValue().set(taskId, "0");
	    //task = taskService.getTaskById(taskId);
	    task = taskService.getTaskById(taskId);
		user = task.getUser();

		long totalNum = workService.getGoodsCount(user, task.getType());
		task.setNum(totalNum);
		task.setStatus("正在读取商品列表");
		taskService.updateTask(task);
		logger.info("task:"+task.getId()+",用户的两个sessionkey:" + user.getSessionKey1() + "," + user.getSessionKey2() + ",商品总数:" + totalNum);
		//taskService.saveTask(task);
		
		/**
		 * 标记任务开始
		 */
		stringRedisTemplate.opsForValue().set("task" + task.getId(), "true");
		
        task.setStatus("正在执行任务");
        taskService.updateTask(task);
        //taskService.saveTask(task);
	}
	/**
	 * 开始任务
	 */
	public void start(){	
		init();			//初始化
		cycleWork();	//循环任务
	}
	/**
	 * 读取商品
	 */
	public void readGoods(long currentPage){
		long totalNum = workService.getGoodsCount(user, task.getType());
		/**
		 * 总页数
		 */
		long countPage;
		if(totalNum % 200 == 0){
			countPage = totalNum / 200;
		}else{
			countPage = totalNum / 200 + 1;
		}
		/**
		 * 若当前页数大于总页数，则从新开始循环
		 */
		if(currentPage >= countPage){
			currentPage = 1;
		}else{
			currentPage ++;
		}
		
		acquiredGoods = workService.getGoodsByQueue(user, task.getType(), currentPage);		//读取出来的所有商品
		
		if(totalNum == 0){
        	finishTask();
        	/**
    		 * 记录任务日志
    		 */
    		Tasklog tasklog = new Tasklog();
    		tasklog.setGoodid("");
        	tasklog.setTask(task);
        	tasklog.setTime(new Date());
        	tasklog.setMsg("没有商品 ，无法开始任务");
        	tasklog.setTitle("");
    		tasklogService.insertTasklog(tasklog);
        	return;
        }
	}
	
	/**
	 * 循环任务
	 */
	public void cycleWork(){
		/**
		 * 限制工作时间
		 */
		taskService.limitMaxTaskRunTime(task, 24);
		
		Date endTime = task.getEndTime();
		logger.info("用户：" + user.getNick() + "开始任务,taskId="+taskId+",任务开始时间:" + task.getStartTime() +"截至时间:" + task.getEndTime());
		
		startTime = System.currentTimeMillis();
		/**
		 * 开始工作
		 */
		while(true){
			/**
			 * 读取商品
			 */
			readGoods(currentPage);
			/**
			 * 开始一轮上下架
			 */
			listringAndDelisting();
			/**
			 * 完整上下架
			 */
			if(endTime == null){
				logger.info("用户：" + user.getNick() + "---任务完成");
				finishTask();		//结束任务
				break;
			}
			/**
			 * 循环上下架
			 */
			if(endTime.before(new Date())){
				logger.info("用户：" + user.getNick() + "---任务完成");
				finishTask();		//结束任务
				break;
			}
			/**
			 * 判断任务运行标记是否存在（即用户是否停止任务）
			 */
			if(TaskRunFlag() == false){
				logger.info("用户：" + user.getNick() + "---中断任务");
				finishTask();		//结束任务
				break;
			}
			logger.info("用户：" + user.getNick() + "---开始一轮上下架");
		}
	}

	/**
	 * 上架和下架
	 */
	public void listringAndDelisting(){
		outerWhile:
		while (true) {
			try{
				Good good = this.getAcquiredGoods().poll();
				/**
				 * 当商品已经获取完后，结束任务
				 */
				if(good == null){
					break outerWhile;
				}
				/**
				 * 有商品，则继续任务
				 */
				while(true){
					/**
					 * 判断是否结束任务
					 */
					isOverTheWork(good);
					ResponseDealResultCode responseCode = null;
					/**
					 * 上下架处理
					 */
					switch(task.getType()){
						case "在售商品" : {
							responseCode = goodService.doGoodDelisting(user, good,task);
							break;
						}
						case "仓库商品" : {
							responseCode = goodService.doGoodListing(user, good,task);
							break;
						}
					}
					TimeUnit.MILLISECONDS.sleep(200);
					/**
					 * 异常响应结果处理
					 */
					switch(responseCode){
						case NEXT:{
							continue outerWhile;
						}
						case SUCCESS:{
							break;
						}
						case OUT:{
							break outerWhile;
						}
					}
					
					/*************************************************************************************/
					/**
					 * 判断是否结束任务
					 */
					isOverTheWork(good);
					/**
					 * 上下架处理
					 */
					switch(task.getType()){
						case "在售商品" : {
							responseCode = goodService.doGoodListing(user, good,task);
							break;
						}
						case "仓库商品" : {
							responseCode = goodService.doGoodDelisting(user, good,task);
							break;
						}
					}
					TimeUnit.MILLISECONDS.sleep(200);
					/**
					 * 异常响应结果处理
					 */
					switch(responseCode){
						case SUCCESS:{
							this.success++;
							continue outerWhile;
						}
						case NEXT:{
							continue outerWhile;
						}
						case PRESENT:{
							break;
						}
						case OUT:{
							break outerWhile;
						}
					}
					
				}
			}catch(Exception e){
				e.printStackTrace();
				break outerWhile;
			}
		}
		
	}
	
	
	/**
	 * 是否结束当前任务
	 * @param good
	 * @return true: 是   false:否
	 */
	public boolean isOverTheWork(Good good){
		/**
		 * 外部条件标记停止任务
		 */
		if(this.TaskRunFlag() == false){
			return true;
		}
		/**
		 * 流量不足
		 */
		if(this.flowUsage(user.getNick(),good) == false){
			this.stopTaskByException();
			return true;
		}
		/**
		 * 时间到
		 */
		if ((task.getEndTime() != null && (task.getEndTime().before(new Date())))) {
			return true;
        }
		return false;
	}
	
	/**
	 * 用时
	 */
	public void usedTime(){
        long now = new Date().getTime();
        long seconds = (now - startTime) / 1000;
        long minute = seconds / 60;
		logger.info("在"+ seconds +"秒内完成了"+this.success+"次，在"+minute+" 分钟内完成了"+this.success +"次，taskId:"+taskId+",真实完成任务:" + this.success);
	}
	
	/**
	 * 完成任务
	 */
	public void finishTask(){
        //redis.remove("task" + task.getId());		//任务完成，清空缓存
		stringRedisTemplate.delete("task" + task.getId());
		/**
        * 用时
        */
        usedTime();
		
        /**
         * 更新任务状态：结束
         */
        task.setEndTime(new Date());
		task.setStatus("任务结束");
        taskService.updateTask(task);
	}
	/**
	 * 任务运行标记
	 * @return
	 */
	public boolean TaskRunFlag() {
		String flagStr = stringRedisTemplate.opsForValue().get("task" + taskId);
		if(flagStr == null || "".equals(flagStr)){
			flag = false;
		}else{
			flag = new Boolean(flagStr);
		}
		return flag;
	}
	
	/**
	 * 任务异常停止
	 */
	public void stopTaskByException(){
		//停止任务
    	stringRedisTemplate.delete("task" + taskId);
	}
	
	/**
	 * 恢复商品列表
	 */
	public void recoveryGoodList(String type) {
        //logService.log(user, task.getDescription() + "任务进度", "开始恢复子任务" + 1);
        logger.info("一共有" + recoverGoods.size() + "件商品需要恢复");
        outerWhile:
        while(true){
        	Good good = recoverGoods.poll();
        	if(good == null){
        		break;
        	}
        	while (true) {
        		ResponseDealResultCode responseCode = null;
				switch (type) {
				case "在售商品": {
					responseCode = goodService.doGoodListing(user, good,task);
					break;
				}
				case "仓库商品": {
					responseCode = goodService.doGoodDelisting(user, good,task);
					break;
				}
				}
				/**
				 * 处理响应结果
				 */
				switch (responseCode) {
					case SUCCESS: {
						//logService.log(user, "恢复商品原状态成功", good.getTitle());
						try {
							TimeUnit.MILLISECONDS.sleep(45);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue outerWhile;		//下一个商品
					}
				}
				/**
				 * 失败处理结果
				 */
				try {
					TimeUnit.MILLISECONDS.sleep(45);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				/**
				 * 异常响应结果处理
				 */
				switch(responseCode){
					case NEXT:{
						continue outerWhile;
					}
					case PRESENT:{
						break;
					}
					case OUT:{
						break outerWhile;
					}
				}
			}
        }
        //logService.log(user, "所有恢复商品原状态成功", "恢复任务完成");
        logger.info("已全部恢复成功");
    }
	
	/**
	 * 流量使用情况
	 * @return true:可用  false：不可用
	 */
	public boolean flowUsage(String nick,Good good){
		String usableFlowObject = stringRedisTemplate.opsForValue().get("usableFlow" + nick);
		long usableFlow = usableFlowObject == null ? 0 : Long.parseLong(usableFlowObject);
		if(usableFlow > 0){
			return true;
		}
		/**
		 * 记录任务日志
		 */
		Tasklog tasklog = new Tasklog();
		tasklog.setGoodid(good.getNumIid() + "");
    	tasklog.setTask(task);
    	tasklog.setTime(new Date());
    	tasklog.setMsg("流量不足，任务停止");
    	tasklog.setTitle(good.getTitle());
		tasklogService.insertTasklog(tasklog);
		return false;
		
	}
	/**
	 * 流量消耗 -1
	 */
	public void flowDecrement(){
		long usableFlow = stringRedisTemplate.opsForValue().decrement("usableFlow" + user.getNick());
		logger.info("当前流量：" + usableFlow);
	}
	
	public BlockingQueue<Good> getAcquiredGoods() {
		return acquiredGoods;
	}

	public void setAcquiredGoods(BlockingQueue<Good> acquiredGoods) {
		this.acquiredGoods = acquiredGoods;
	}
	
	public BlockingQueue<Integer> getEndTaskFlag() {
		return endTaskFlag;
	}

	public void setEndTaskFlag(BlockingQueue<Integer> endTaskFlag) {
		this.endTaskFlag = endTaskFlag;
	}

	public BlockingQueue<Good> getRecoverGoods() {
		return recoverGoods;
	}

	public void setRecoverGoods(BlockingQueue<Good> recoverGoods) {
		this.recoverGoods = recoverGoods;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public BlockingQueue<Good> getDealGoods() {
		return dealGoods;
	}

	public void setDealGoods(BlockingQueue<Good> dealGoods) {
		this.dealGoods = dealGoods;
	}

	public RedisService getRedis() {
		return redis;
	}

	public void setRedis(RedisService redis) {
		this.redis = redis;
	}
	
}











