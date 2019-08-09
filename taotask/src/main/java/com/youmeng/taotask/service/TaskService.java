package com.youmeng.taotask.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youmeng.common.base.taotask.entity.Task;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-06
 */
public interface TaskService{
	/**
	 * 根据任务获取任务日志 
	 */
	public Task getTaskLogsByTask(String id);
	/**
	 * 包括User
	 * @param id	taskId
	 * @return
	 */
	public Task getTaskById(String id);
	/**
	 * 更新任务
	 * @param task
	 * @return
	 */
	public int updateTask(Task task);
	/**
	 * 限制任务最大的运行时间
	 * @param task
	 * @param i
	 */
	public void limitMaxTaskRunTime(Task task, int i);
}




