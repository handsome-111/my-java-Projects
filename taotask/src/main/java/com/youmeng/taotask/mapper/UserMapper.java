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
public interface UserMapper extends BaseMapper<Task> {

}
