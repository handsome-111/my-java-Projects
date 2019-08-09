package com.youmeng.taotask.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youmeng.common.base.taotask.entity.Tasklog;
import com.youmeng.taotask.mapper.TasklogMapper;
import com.youmeng.taotask.service.TasklogService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-06
 */
@Service
public class TasklogServiceImpl extends ServiceImpl<TasklogMapper, Tasklog> implements TasklogService {
	@Autowired
	private TasklogMapper tasklogMapper;

	@Override
	public int insertTasklog(Tasklog tasklog) {
		return tasklogMapper.insertTaskLog(tasklog);
	}
	
}
