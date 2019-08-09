package com.youmeng.taotask.serviceImpl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youmeng.common.base.taotask.entity.ResponseMessage;
import com.youmeng.taotask.mapper.ResponseMessageMapper;
import com.youmeng.taotask.service.ResponseMessageService;

/**
 * <p>
 * 调用淘宝接口响应代码，主要获取淘宝最新的错误信息来及时更新应用 服务实现类
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-07
 */
@Service
public class ResponseMessageServiceImpl extends ServiceImpl<ResponseMessageMapper, ResponseMessage> implements ResponseMessageService {
}
