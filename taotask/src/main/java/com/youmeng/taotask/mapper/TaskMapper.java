package com.youmeng.taotask.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youmeng.common.base.taotask.entity.Task;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-06
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {
	/**
	 * 根据任务获取任务日志 
	 */
	Task getTaskLogsByTask(String id);
	/**
	 * 包括User
	 * @param id	taskId
	 * @return
	 */
	Task getTaskById(String id);
	/**
	 * 更新任务
	 * @param task
	 * @return
	 */
	int updateTask(Task task);
}
