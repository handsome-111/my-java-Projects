package com.youmeng.taoshelf.serviceImpl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youmeng.taoshelf.entity.ResponseMessage;
import com.youmeng.taoshelf.mapper.ResponseMessageMapper;
import com.youmeng.taoshelf.service.ResponseMessageService;

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
