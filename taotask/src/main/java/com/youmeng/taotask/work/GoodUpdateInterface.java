package com.youmeng.taotask.work;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.youmeng.common.base.taotask.entity.Good;
import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.common.base.taotask.entity.User;
import com.youmeng.common.base.taotask.response.ResponseDealResultCode;
import com.youmeng.taotask.service.GoodService;

/**
 * 更新接口上下架任务
 * @author Administrator
 *
 */
public class GoodUpdateInterface implements Runnable{
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
	private GoodService goodService;
	
	private Task task;
    private GoodCycle goodCycle;
	public GoodUpdateInterface(GoodCycle goodCycle){
		this.goodCycle = goodCycle;
		this.task = goodCycle.getTask();
	}
	@Override
	public void run() {
		work();
	}
	
	public void work(){
		/**
		 * 获取任务信息和用户信息
		 */
		Date endTime = goodCycle.getTask().getEndTime();
		User user = goodCycle.getUser();
		outerWhile:
		while (true) {
			try{
				Good good = goodCycle.getAcquiredGoods().poll();
				/**
				 * 当商品已经获取完后，结束任务
				 */
				if(good == null){
					break outerWhile ;
				}
				/**
				 * 外部条件标记停止任务
				 */
				if(goodCycle.TaskRunFlag() == false){
					break outerWhile;
				}
				/**
				 * 时间到
				 */
				if ((endTime != null && endTime.before(new Date()))) {
		            break outerWhile;
		        }
				ResponseDealResultCode responseCode = null;
				
				/**
				 * 先上/下架处理
				 */
				outerFirst:
				while(true){
					responseCode = goodService.updateGood(user, good,task);
					/**
					 * 成功响应结果处理
					 */
					switch(responseCode){
						case SUCCESS:{
							break outerFirst;
						}
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
				/*--------------------------------------------------------------------------------------*/
				/**
				 * 第二步上下架任务
				 */
				outerSeconds:
				while(true){
					responseCode = goodService.updateGood(user, good,task);
					/**
					 * 成功响应结果处理
					 */
					switch(responseCode){
						case SUCCESS:{
							goodCycle.getSuccess().incrementAndGet();		//将商品放入待处理商品列表中
							break outerSeconds;
						}
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
			
			}catch(Exception e){
				/**
				 * 中止任务
				 */
				e.printStackTrace();
				break outerWhile;
			}
		}
		/**
		 * 结束任务,告知主线程
		 */
		while(true){
			logger.info("结束第三个任务");
			try {
				goodCycle.getEndTaskFlag().put(3);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();	
			}
		}
	}
	/*
	public ResponseCode firstWork(User user,Good good){
		*//**
		 * 先上/下架处理
		 *//*
		responseCode = goodService.updateGood(user, good);
		
	}
	public ResponseCode afterWork(User user,Good good){
		
	}*/
}
