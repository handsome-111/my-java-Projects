package com.youmeng.taoshelf.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youmeng.taoshelf.service.GoodService;
/**
 * 恢复商品线程
 * @author Administrator
 *
 */
public class RecoveryGoodTask implements Runnable{
	private MainTask work;
    private Logger logger =  LoggerFactory.getLogger(getClass());
    private boolean flag = true;
	public RecoveryGoodTask(MainTask work) {
		this.work = work;
	}
	@Override
	public void run() {
		logger.info("完成中断任务，开始恢复商品大小：" + work.getRecoverGoods().size());
		this.recoverGood();
		work.finishTask();
		work.removeTask();
	}
	/**
	 * 恢复商品
	 */
	public void recoverGood(){
		while(flag){
			try{
				work.recoveryGoodList();
				flag = false;
			}catch(Exception e){
				flag = true;
				e.printStackTrace();
			}
		}
		logger.info("恢复完成");
	}
}
