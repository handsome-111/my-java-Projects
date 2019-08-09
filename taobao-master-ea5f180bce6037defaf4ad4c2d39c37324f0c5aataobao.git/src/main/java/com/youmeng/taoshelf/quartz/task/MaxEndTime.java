package com.youmeng.taoshelf.quartz.task;

/**
 * 最大的结束时间
 * @author Administrator
 *
 */
public enum MaxEndTime {
	MAXENDTIME;
	private long maxTime = 24 * 60 * 60 * 1000;
	public long getTime(){
		return maxTime;
	}
}
