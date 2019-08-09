package com.youmeng.taotask.work;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.youmeng.common.base.taotask.entity.Good;
import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.common.base.taotask.entity.User;
import com.youmeng.common.base.taotask.response.ResponseDealResultCode;
import com.youmeng.taotask.service.GoodService;
import com.youmeng.taotask.service.TaskService;
import com.youmeng.taotask.service.WorkService;
import com.youmeng.taotask.util.SpringContextUtils;

/**
 * 商品下架
 * @author Administrator
 *
 */
public class GoodDelisting {

	private GoodService goodService = SpringContextUtils.getBean(GoodService.class);
    private Logger logger = LoggerFactory.getLogger(getClass());
    private StringRedisTemplate stringRedisTemplate = SpringContextUtils.getBean(StringRedisTemplate.class);;
    private TaskService taskService = SpringContextUtils.getBean(TaskService.class);
    private WorkService workService = SpringContextUtils.getBean(WorkService.class);
    
	private Task task;
	private boolean flag;
	private Map<Integer,List<Good>> acquiredGoods;		//读取得的商品
	private User user;
	private String taskId;
	private int success = 0;
	private long startTime;

	public GoodDelisting(String taskId){
		this.taskId = taskId;
	}
	public void init(){
		task = taskService.getTaskById(taskId);
        user = task.getUser();
    	logger.info("用户的两个sessionkey:" + user.getSessionKey1() + "," + user.getSessionKey2());
    	
    	
        long totalNum = workService.getGoodsCount(user, task.getType());
        task.setNum(totalNum);
        task.setStatus("正在读取商品列表");
        taskService.updateTask(task);
        /**
         * 获取分页
         */
        long page = 1;
        if(totalNum % 200 == 0){
        	page = totalNum / 200;
        }else{
        	page = totalNum / 200 + 1;
        }
        acquiredGoods = workService.getGoodsByCategory(user, task.getType(), page);		//读取出来的所有商品
		stringRedisTemplate.opsForValue().set("task" + task.getId(), "true");        
		if(totalNum == 0){
        	finishTask();
        	logger.info("没有商品无法上下架");
        	return;
        }
        task.setStatus("正在执行任务");
        taskService.updateTask(task);
        
	}
	/**
	 * 开始工作
	 */
	public void start(){
		/**
		 * 初始化工作
		 */
		init();
		/**
		 * 开始任务
		 */
		startTime = System.currentTimeMillis();
		try {
			Set<Integer> keys = acquiredGoods.keySet();
			Iterator<Integer> ite = keys.iterator();
			/**
			 * 迭代所有商品
			 */
			outerWhile: while (ite.hasNext()) {
				List<Good> goods = acquiredGoods.get(ite.next());
				/**
				 * 迭代每个类目的商品
				 */
				outerFor: for (Good good : goods) {
					while (true) {
						/**
						 * 检测是否关闭
						 */
						if(isFlag() == false){
							break outerWhile;
						}
						if(flowUasge(user.getNick()) == false){
							break outerWhile;
						}
						/**
						 * 调用接口
						 */
						ResponseDealResultCode responseCode = null;
						responseCode = goodService.doGoodDelisting(user, good,task);
						TimeUnit.MILLISECONDS.sleep(500);
						/**
						 * 成功响应结果处理
						 */
						switch(responseCode){
							case SUCCESS:{
								success++;
								continue outerFor;
							}
						}
						/**
						 * 异常响应结果处理
						 */
						switch(responseCode){
							case NEXT:{
								continue outerFor;
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
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}finally{
			/**
	         * 完成任务
	         */
	        finishTask();
		}
	}
	
	/**
	 * 完成任务
	 */
	public void finishTask(){
		stringRedisTemplate.delete("task" + task.getId());
		/**
		 * 用时
		 */
        usedTime();
		
		task.setEndTime(new Date());
		task.setStatus("任务结束");
        taskService.updateTask(task);
	}
	
	/**
	 * 流量使用情况
	 * @return true:可用  false：不可用
	 */
	public boolean flowUasge(String nick){
		String usableFlowObject = stringRedisTemplate.opsForValue().get("usableFlow" + nick);
		long usableFlow = usableFlowObject == null ? 0 : Long.parseLong(usableFlowObject);
		if(usableFlow > 0){
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
	
	public boolean isFlag() {
		String flagStr = stringRedisTemplate.opsForValue().get("task" + taskId);
		if(flagStr == null || "".equals(flagStr)){
			flag = false;
		}else{
			flag = new Boolean(flagStr);
		}
		return flag;
	}

}
