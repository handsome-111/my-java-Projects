package com.forezp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forezp.domain.User;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mr.hj
 * @since 2019-09-02
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
	User selectByUsername(@Param("username")String username);
}
