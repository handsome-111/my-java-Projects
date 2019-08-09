package com.youmeng.taoshelf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.youmeng.taoshelf.entity.ResponseMessage;

/**
 * <p>
 * 调用淘宝接口响应代码，主要获取淘宝最新的错误信息来及时更新应用 服务类
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-07
 */
public interface ResponseMessageService extends IService<ResponseMessage> {

}
