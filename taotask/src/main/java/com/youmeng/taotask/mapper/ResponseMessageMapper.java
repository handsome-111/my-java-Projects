package com.youmeng.taotask.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youmeng.common.base.taotask.entity.ResponseMessage;

/**
 * <p>
 * 调用淘宝接口响应代码，主要获取淘宝最新的错误信息来及时更新应用 Mapper 接口
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-07
 */
@Mapper
public interface ResponseMessageMapper extends BaseMapper<ResponseMessage> {

}
