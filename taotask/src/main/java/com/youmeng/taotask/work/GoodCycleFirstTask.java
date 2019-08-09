package com.youmeng.taotask.work;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.youmeng.common.base.taotask.entity.Good;
import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.common.base.taotask.entity.User;
import com.youmeng.common.base.taotask.response.ResponseDealResultCode;
import com.youmeng.taotask.service.GoodService;
import com.youmeng.taotask.util.SpringContextUtils;

/**
 * 循环上下架第一个任务
 * @author Administrator
 *
 */
public class GoodCycleFirstTask implements Runnable{
	private GoodCycle goodCycle;
    private Logger logger = LoggerFactory.getLogger(getClass());
	private GoodService goodService = SpringContextUtils.getBean(GoodService.class);
	private Task task;

	public GoodCycleFirstTask(GoodCycle goodCycle){
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
		Date endTime = task.getEndTime();
		String type = task.getType();
		User user = goodCycle.getUser();
		
		outerWhile:
		while (true) {
			try{
				Good good = goodCycle.getAcquiredGoods().poll();
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
					 * 外部条件标记停止任务
					 */
					if(goodCycle.TaskRunFlag() == false){
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
					 * 时间到
					 */
					if ((endTime != null && endTime.before(new Date()))) {
			            break outerWhile;
			        }
					ResponseDealResultCode responseCode = null;
					/**
					 * 上下架处理
					 */
					switch(type){
						case "在售商品" : {
							responseCode = goodService.doGoodDelisting(user, good,task);
							break;
						}
						case "仓库商品" : {
							responseCode = goodService.doGoodListing(user, good,task);
							break;
						}
					}
					TimeUnit.MILLISECONDS.sleep(1000);
					/**
					 * 成功响应结果处理
					 */
					switch(responseCode){
						case SUCCESS:{
							goodCycle.getDealGoods().add(good);		//将商品放入待处理商品列表中
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
			}catch(Exception e){
				e.printStackTrace();
				break outerWhile;
			}
		}
		/**
		 * 结束任务,告知主线程
		 */
		goodCycle.breakDown();		//结束第二个任务
		while(true){
			try {
				goodCycle.getEndTaskFlag().put(1);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
