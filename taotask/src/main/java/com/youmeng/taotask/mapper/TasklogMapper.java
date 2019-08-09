package com.youmeng.taotask.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youmeng.common.base.taotask.entity.Tasklog;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-06
 */
@Mapper
public interface TasklogMapper extends BaseMapper<Tasklog> {
	public int insertTaskLog(Tasklog tasklog);
	/**
	 * 根据taskId获取任务日志
	 * @param page
	 * @param taskId
	 * @return
	 */
	public IPage<Tasklog> geTasklogByTaskId(Page page,@Param("taskId") String taskId,@Param("order") String order,@Param("field")String field);
}
