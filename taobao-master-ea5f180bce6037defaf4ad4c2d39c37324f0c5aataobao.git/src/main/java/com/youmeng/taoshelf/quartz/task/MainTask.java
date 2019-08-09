package com.youmeng.taoshelf.quartz.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.youmeng.taoshelf.entity.Good;
import com.youmeng.taoshelf.entity.Task;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.quartz.Job1;
import com.youmeng.taoshelf.service.GoodService;
import com.youmeng.taoshelf.service.LogService;
import com.youmeng.taoshelf.service.ResponseCode;
import com.youmeng.taoshelf.service.TaskService;
/**
 * 上下架任务
 * @author Administrator
 *
 */
public class MainTask {
	private GoodService goodService;
	private LogService logService;
    private StringRedisTemplate redisTemplate;
	
	private Map<Integer,List<Good>> acquiredGoods = new HashMap<Integer,List<Good>>();	//读取到的商品
	private List<Good> recoverGoods = new LinkedList<Good>();	//需要恢复的商品
	private BlockingQueue<Good> dealGoods = new LinkedBlockingQueue<Good>();	//需要处理上下架的商品
	private User user;
    private Logger logger = LoggerFactory.getLogger(getClass());
	private String taskId;
	private String type;
	private Date endTime;
	private Task task;
	private TaskService taskService;
	private int dealCount;	//成功处理次数
	private boolean flag = true;
	private boolean recoverFlag = false;	//恢复线程标记
	private BlockingQueue<Integer> flagQueue = new LinkedBlockingQueue<Integer>();
	public MainTask(Task task){
		this.task = task;
		this.taskId = task.getId();
	}
	public MainTask(User user,String type,Map<Integer,List<Good>> acquiredGoods,Task task){
		this.user = user;
		this.taskId = task.getId();
		this.type = type;
		this.acquiredGoods = acquiredGoods;
		this.task = task;
	}
	/**
	 * 循环上下架
	 */
	public void cycleWork(){
		/**
		 * 获取结束时间和设置结束时间
		 */
		endTime = getEndTime();
		Date startTime = task.getStartTime();
		long oneDay = MaxEndTime.MAXENDTIME.getTime();
		/**
		 * 最长的结束时间为24小时
		 */
		if(endTime != null && (endTime.getTime() - startTime.getTime() > oneDay)){
			endTime = new Date(startTime.getTime() + oneDay);
			task.setEndTime(endTime);
		}
		taskService.saveTask(task);
		logger.info("开始时间:" + task.getStartTime() +"截至时间:" + endTime);
		while(flag){
			work();
			/**
			 * 完整上下架
			 */
			if(endTime == null){
				logger.info("任务完成：------------");
				breakDown();		//结束任务
				break;
			}
			/**
			 * 循环上下架
			 */
			if(endTime.before(new Date())){
				logger.info("任务完成：------------");
				breakDown();		//结束任务
				break;
			}
			if(flag == false){
				logger.info("中断任务");
				break;
			}
			/**
			 * 当完成一轮任务，则进行一次恢复：
			 */
			recoverFlag = true;
			synchronized(this){
				logger.info("开始一轮上下架");
				this.recoveryGoodList();
				recoverFlag = false;
				this.notifyAll();
			}
		}
	} 
	/**
	 * 上下架任务
	 */
	public void work(){
		try{
			Set<Integer> keys = acquiredGoods.keySet();
			Iterator<Integer> ite = keys.iterator();
			int add = 0;
			/**
			 * 迭代所有商品
			 */
			outerWhile:		
			while(ite.hasNext()){
				List<Good> goods = acquiredGoods.get(ite.next()); 
				/**
				 * 迭代每个类目的商品
				 */
				outerFor:
				for(Good good : goods){
					while(true){
						if(flag == false){
							throw new InterruptedException("抛出异常，中止mainTask任务");
						}
						if ((endTime != null && endTime.before(new Date())) || !ite.hasNext()) {
				            break outerWhile;
				        }
						ResponseCode responseCode = null;
						/**
						 * 上下架处理
						 */
						switch(type){
							case "在售商品" : {responseCode = goodService.doGoodDelisting(user, good);break;}
							case "仓库商品" : {responseCode = goodService.doGoodListing(user, good);break;}
						}
						
						/**
						 * 成功响应结果处理
						 */
						switch(responseCode){
							case ITEM_UPDATE_LISTING_RESPONSE:{
								dealGoods.add(good);		//将商品放入待处理商品列表中
								TimeUnit.MILLISECONDS.sleep(45); 	//70	45
								continue outerFor;
							}
						}
						add ++;
						if(endTime != null && add % 3 == 0){
							redisTemplate.opsForValue().increment(taskId, 1);
						}
						/**
						 * 异常响应结果处理
						 */
						TimeUnit.MILLISECONDS.sleep(45);	//70	45
						switch(responseCode){
							case ERROR_CODE: { 					//其他错误，则跳出该商品的上下架
		                 		continue outerFor;
		                 	}		
		                 	case ERROR_CODE_15_TIMEOUT : {		//远程服务调用超时
		                 		break;
		                 		//continue outerFor;
		                 	}
		                 	case ERROR_CODE_50_IC_CHECKSTEP_NO_PERMISSION : { 	//账号被处罚，跳出迭代结束任务
		                 		throw new Exception("账号被处罚，结束任务");
		                 	}	
		                 	case ERROR_CODE_50_QUANTITY_ITEM_CAT_TOO_LARGE : { 	//类目超额，跳出本次类目迭代
		                 		continue outerWhile;
		                 	}		
		                 	case ERROR_CODE_530_BUSY : {						//调用接口频率太快
		                 		TimeUnit.MILLISECONDS.sleep(50);
		                 		break;
		                 		//continue outerFor;
		                 	}
		                 	case ERROR_CODE_7_LIMITED_BY_API_ACCESS_COUNT : {	//访问控制,受api访问计数限制
		                 		break;
		                 		//continue outerFor;
		                 	}
						}
					
					}
				}
			}
		}catch(Exception e){
			logger.info("结束第一个任务线程");
			e.printStackTrace();
			flag = false;
			return ;
		}finally{
			try {
				flagQueue.put(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 中止任务
	 */
	public void breakDown(){
		this.flag = false;
		task.setStatus("正在恢复商品");
        taskService.saveTask(task);
        
        try {
			flagQueue.take();
	        flagQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        long now = new Date().getTime();
        long seconds = (now - task.getStartTime().getTime()) / 1000;
        long minute = seconds / 60;
		logger.info("在"+ seconds +"秒内完成了"+this.dealCount+"次，在"+minute+" 分钟内完成了"+this.dealCount +"次，taskId:"+taskId+",真实完成任务:" + this.dealCount + ",value:" + redisTemplate.opsForValue().get(taskId));
		Job1.exec.execute(new RecoveryGoodTask(this));		//开启恢复线程
		/*try {
			Thread thread = Job1.secondsThreads.get(taskId);
			Thread thread2 = Job1.firstThreads.get(taskId);
			if (thread2 != null) {
				if(thread2.isAlive()){
					thread2.interrupt(); //中断线程
				}
			} 
			if(thread != null){
				if(thread.isAlive()){
					thread.interrupt(); //中断线程
				}
			}
		} catch (Exception e) {
			logger.info("中止任务出现异常，，，");
			e.printStackTrace();
		} finally{
			logger.info("完成中断线程任务");
			task.setStatus("正在恢复商品");
	        taskService.saveTask(task);
			Job1.exec.execute(new RecoveryGoodTask(this));		//开启恢复线程
		}*/
	}
	/**
	 * 上下架工作完成
	 */
	public void workCompletion(){
		this.dealCount++;
		if(getEndTime() != null){
			redisTemplate.opsForValue().increment(taskId, 1);
		}
	}
	/**
	 * 删除任务
	 */
	public void removeTask(){
		logger.info("删除任务 :" + taskId);
		Job1.jobs.remove(taskId);
		Job1.firstThreads.remove(taskId);
		Job1.secondsThreads.remove(taskId);
	}
	
	 /**
	  * 恢复商品列表
	  */
    public void recoveryGoodList() {
        logService.log(user, task.getDescription() + "任务进度", "开始恢复子任务" + 1);
        logger.info("一共有" + recoverGoods.size() + "件商品需要恢复");
        outerFor:
        while(!recoverGoods.isEmpty()){
        	Good good = recoverGoods.get(0);
        	recoverGoods.remove(good);		//从列表中移除
        	ResponseCode responseCode = null;
        	while(true){
        		switch(type){
        			case "在售商品" : {responseCode = goodService.doGoodListing(user, good);break;}
        			case "仓库商品" : {responseCode = goodService.doGoodDelisting(user, good);break;}
        		}
	        	/**
				 * 处理响应结果
				 */
        		switch(responseCode){
	        		case ITEM_UPDATE_LISTING_RESPONSE:{
						//redisTemplate.opsForValue().increment(taskId, 1);
			            logService.log(user, "恢复商品原状态成功", good.getTitle());
			            try {
							TimeUnit.MILLISECONDS.sleep(45);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			            continue outerFor;
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
        		switch(responseCode){
					case ERROR_CODE: { 					//其他错误，则跳出该商品的上下架
                 		continue outerFor;
                 	}		
                 	case ERROR_CODE_15_TIMEOUT : {		//远程服务调用超时
                 		break;
                 	}
                 	case ERROR_CODE_50_IC_CHECKSTEP_NO_PERMISSION : { 	//账号被处罚，跳出迭代结束任务
                 		break outerFor;
                 	}	
                 	case ERROR_CODE_50_QUANTITY_ITEM_CAT_TOO_LARGE : { 	//类目超额，跳出本次类目迭代
                 		logger.info("恢复商品失败，类目超额");
                 		continue outerFor;
                 	}		
                 	case ERROR_CODE_530_BUSY : {						//调用接口频率太快
                 		break;
                 	}
                 	case ERROR_CODE_7_LIMITED_BY_API_ACCESS_COUNT : {	//访问控制,受api访问计数限制
                 		break;
                 	}
				}
        	}
        }
        logService.log(user, "所有恢复商品原状态成功", "恢复任务完成");
        logger.info("已全部恢复成功");
    }
	public StringRedisTemplate getRedisTemplate() {
		return redisTemplate;
	}
	/**
	 * 结束时间
	 * @return
	 */
	public Date getEndTime() {
		return taskService.findTaskById(taskId).getEndTime();
	}
	
	/**
	 * 完成任务
	 */
	public void finishTask(){
		task.setEndTime(new Date());
		logger.info("taskId:"+taskId+",完成任务:" + redisTemplate + ",value:" + redisTemplate.opsForValue().get(taskId));
		//task.setStatus("任务结束(成功处理" + redisTemplate.opsForValue().get(taskId) + "次)");
		task.setStatus("任务结束");
        taskService.saveTask(task);
        logger.info("保存信息成功");
        redisTemplate.delete(taskId);
	}

	public String getType() {
		return type;
	}
	public User getUser() {
		return user;
	}
	public BlockingQueue<Good> getDealGoods() {
		return dealGoods;
	}
	public Map<Integer, List<Good>> getAcquiredGoods() {
		return acquiredGoods;
	}
	public List<Good> getRecoverGoods() {
		return recoverGoods;
	}
	public GoodService getGoodService() {
		return goodService;
	}
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	public void setGoodService(GoodService goodService) {
		this.goodService = goodService;
	}
	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public boolean isRecoverFlag() {
		return recoverFlag;
	}
	public void setRecoverFlag(boolean recoverFlag) {
		this.recoverFlag = recoverFlag;
	}
	
	public BlockingQueue<Integer> getFlagQueue() {
		return flagQueue;
	}
	public void setFlagQueue(BlockingQueue<Integer> flagQueue) {
		this.flagQueue = flagQueue;
	}
	@Override
	public String toString() {
		return "DesenoTask [goodService=" + goodService + ", logService=" + logService + ", redisTemplate="
				+ redisTemplate + ", acquiredGoods=" + acquiredGoods + ", recoverGoods=" + recoverGoods + ", dealGoods="
				+ dealGoods + ", user=" + user + ", logger=" + logger + ", taskId=" + taskId + ", type=" + type
				+ ", endTime=" + endTime + ", task=" + task + ", taskService=" + taskService + ", dealCount="
				+ dealCount + "]";
	}
}
