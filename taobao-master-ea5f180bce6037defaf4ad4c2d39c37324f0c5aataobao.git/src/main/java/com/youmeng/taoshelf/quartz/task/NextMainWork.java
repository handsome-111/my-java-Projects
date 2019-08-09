package com.youmeng.taoshelf.quartz.task;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisSystemException;

import com.youmeng.taoshelf.entity.Good;
import com.youmeng.taoshelf.quartz.Job1;
import com.youmeng.taoshelf.service.GoodService;
import com.youmeng.taoshelf.service.ResponseCode;
/**
 * 执行上下架任务(第二轮)
 * @author Administrator
 *
 */
public class NextMainWork implements Runnable{
	private MainTask work;
    private Logger logger = LoggerFactory.getLogger(getClass());

	private GoodService goodService;
	public NextMainWork(MainTask work) {
		this.work = work;
	}
	
	@Override
	public void run() {
		synchronized (work) {
			Job1.secondsThreads.put(work.getTaskId(), Thread.currentThread());
			String type = work.getType(); //任务类型：在售商品上下架     /   仓库商品上下架

			outerWhile: while (true) {
				int i = 0;
				Good good = null;
				try {
					Date endTime = work.getEndTime();
					if(work.isRecoverFlag() == true){
						logger.info("停止当前工作，让给恢复线程");
						work.wait();     //完成一轮上下架，先让商品进行恢复
					}
					if (work.isFlag() == false) {
						throw new InterruptedException("抛出异常，中止mainTask任务");
					}
					if (work.getDealGoods().size() == 0 && endTime != null && endTime.before(new Date())) {
						break outerWhile;
					}
					good = work.getDealGoods().take(); //堵塞获取上一步已经完成的商品
					ResponseCode responseCode = null;

					/**
					 * 上下架处理
					 */
					outerWhile2: while (true) {
						switch (type) {
						case "在售商品": {
							responseCode = goodService.doGoodListing(work.getUser(), good);
							break;
						}
						case "仓库商品": {
							responseCode = goodService.doGoodDelisting(work.getUser(), good);
							break;
						}
						}
						/**
						 * 成功响应结果处理
						 */
						switch (responseCode) {
						case ITEM_UPDATE_LISTING_RESPONSE: {
							work.workCompletion(); //完成一次上下架
							TimeUnit.MICROSECONDS.sleep(45);
							continue outerWhile;
						}
						}
						/**
						 * 失败响应结果处理
						 */
						switch (responseCode) {
						case ERROR_CODE_50_IC_CHECKSTEP_NO_PERMISSION: {
							Thread.currentThread().interrupt();
							break;
						}
						case ERROR_CODE_530_BUSY: {
							TimeUnit.MILLISECONDS.sleep(100);
							break;
						}
						case ERROR_CODE_7_LIMITED_BY_API_ACCESS_COUNT: {
							TimeUnit.MILLISECONDS.sleep(45);
							break;
						}
						case ERROR_CODE_50_QUANTITY_ITEM_CAT_TOO_LARGE: { //类目超额，跳出本次类目迭代
							continue outerWhile;
						}
						}

						/**
						 * 尝试3次进行还原，否则放入恢复商品中
						 *//*
						i++;
						if (i == 3) {
							work.getRecoverGoods().add(good); //处理失败，将商品放入恢复商品中
							break;
						}*/
					}

				} catch (Exception e) {
					e.printStackTrace();
					BlockingQueue<Good> dealGoods = work.getDealGoods();
					for (Good g : dealGoods) {
						work.getRecoverGoods().add(g);
					}
					if (good != null) {
						work.getRecoverGoods().add(good); //处理失败，将商品放入恢复商品中
					}
					logger.info("成功中断第二个任务并结束");
					work.setFlag(false);
					try {
						work.getFlagQueue().put(2);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return;
				} 
			}
		}
	}

	public void setGoodService(GoodService goodService) {
		this.goodService = goodService;
	}
	/**
	 * 因为时间到了，将已完成第一步的商品放到待恢复列表中
	 */
	public void putRecoverGoods(){
		for(Good good : work.getDealGoods()){
			work.getRecoverGoods().add(good);
		}
	}
}




