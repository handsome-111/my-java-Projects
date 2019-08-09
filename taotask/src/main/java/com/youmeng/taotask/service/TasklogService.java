package com.youmeng.taotask.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.youmeng.common.base.taotask.entity.Tasklog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-06
 */
public interface TasklogService extends IService<Tasklog> {
	int insertTasklog(Tasklog tasklog);
}
