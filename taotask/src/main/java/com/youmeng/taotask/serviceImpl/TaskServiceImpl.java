package com.youmeng.taotask.serviceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.taotask.mapper.TaskMapper;
import com.youmeng.taotask.service.TaskService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-06
 */
@Service
public class TaskServiceImpl implements TaskService{
	@Autowired
	private TaskMapper taskMapper;
	/**
	 * 根据任务获取任务日志 
	 */
	public Task getTaskLogsByTask(String id){
		return taskMapper.getTaskLogsByTask(id);
	}
	/**
	 * 包括User
	 * @param id	taskId
	 * @return
	 */
	public Task getTaskById(String id){
		return taskMapper.getTaskById(id);
	}
	/**
	 * 更新任务
	 * @param task
	 * @return
	 */
	public int updateTask(Task task){
		return taskMapper.updateTask(task);
	}
	/**
	 * 限制任务最大的运行时间
	 * @param task
	 * @param i
	 */
	public void limitMaxTaskRunTime(Task task, int i) {
		long startTime = task.getStartTime().getTime();
		long endTime = task.getEndTime().getTime();
		long limitTime = 1000 * 60 * 60 * i;
		/**
		 * 任务运行时间
		 */
		long runTime = endTime - startTime;
		if(runTime > limitTime){
			Date end = new Date(startTime + limitTime);
			task.setEndTime(end);
			taskMapper.updateTask(task);
		}
	}
}




