package com.youmeng.taotask.work;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisSystemException;

import com.youmeng.common.base.taotask.entity.Good;
import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.common.base.taotask.entity.User;
import com.youmeng.common.base.taotask.response.ResponseDealResultCode;
import com.youmeng.taotask.service.GoodService;
import com.youmeng.taotask.util.SpringContextUtils;

public class GoodCycleSecondTask implements Runnable{
	private GoodCycle goodCycle;
    private Logger logger = LoggerFactory.getLogger(getClass());


	private User user;
	private Task task;
	private GoodService goodService = SpringContextUtils.getBean(GoodService.class);
	public GoodCycleSecondTask(GoodCycle goodCycle){
		this.goodCycle = goodCycle;
		this.task = goodCycle.getTask();
	}
	
	@Override
	public void run() {
		work();
	}

	public void work(){
		logger.info("开启第二个任务");
		/**
		 * 获取任务信息和用户信息
		 */
		String type = goodCycle.getTask().getType(); 	//任务类型：在售商品上下架     /   仓库商品上下架
		Date endTime = goodCycle.getTask().getEndTime();
		user = goodCycle.getUser();
		
		outerWhile: 
		while (true) {
			Good good = null;
			try {
				/**
				 * 结束任务
				 */
				if (Thread.currentThread().isInterrupted()) {
					break outerWhile;
				}
				/**
				 * 流量不足
				 */
				if(goodCycle.flowUsage(user.getNick(),good) == false){
					goodCycle.stopTaskByException();
					break outerWhile;
				}
				/**
				 * 根据时间结束任务
				 */
				if (goodCycle.getDealGoods().size() == 0 && endTime != null && endTime.before(new Date())) {
					break outerWhile;
				}
				good = goodCycle.getDealGoods().take(); //堵塞获取上一步已经完成的商品
				ResponseDealResultCode responseCode = null;
				
				/**
				 * 上下架处理
				 */
				while (true) {
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
					TimeUnit.MILLISECONDS.sleep(1000);
					/**
					 * 成功响应结果处理
					 */
					switch (responseCode) {
						case SUCCESS: {
							goodCycle.getSuccess().incrementAndGet();		//完成一次上下架
							continue outerWhile;
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
			} catch (Exception e) {
				e.printStackTrace();
				break outerWhile;
			}
		}
		
		restoreGoods();		//恢复商品
		while (true) {
			try {
				goodCycle.getEndTaskFlag().put(2);
				break;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} 
		}
	
	
	}
	
	/**
	 * 恢复商品
	 */
	public void restoreGoods(){
		logger.info("开始恢复商品");
		BlockingQueue<Good> restoreGoods = this.goodCycle.getDealGoods();
		outerWhile:
		while(true){
			Good good = restoreGoods.poll();
			if(good == null){
				break outerWhile;
			}
			try {
				if(goodCycle.flowUsage(user.getNick(),good) == false){
					break outerWhile;
				}

				ResponseDealResultCode responseCode = null;
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
				TimeUnit.MILLISECONDS.sleep(300);
				/**
				 * 成功响应结果处理
				 */
				switch(responseCode){
					case SUCCESS:{
						continue outerWhile;
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
			} catch (RedisSystemException re){
				
			} catch(InterruptedException it){
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("恢复任务结束");
	}
}
