package com.youmeng.taotask.service;

import org.springframework.stereotype.Service;

import com.youmeng.common.base.taotask.entity.Good;
import com.youmeng.common.base.taotask.entity.ResponseMessage;
import com.youmeng.common.base.taotask.entity.Result;
import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.common.base.taotask.entity.User;
import com.youmeng.common.base.taotask.response.ResponseCode;
import com.youmeng.common.base.taotask.response.ResponseDealResultCode;

public interface GoodService {
    //获取在售
    public Result<Good> getGoodsOnsale(User user, String q, Long page_size, Long page_no);

    //获取库存
    public Result<Good> getGoodsInstock(User user, String q, Long page_size, Long page_no);

     
    //上架
    public ResponseDealResultCode doGoodListing(User user, Good good,Task task);

    //下架
    public ResponseDealResultCode doGoodDelisting(User user, Good good,Task task);
	
    /**
     * 更新一个商品
     * @return
     */
    public ResponseDealResultCode updateGood(User user,Good good,Task task);
    
    /**
     * 修改一个商品的价格
     */
    public ResponseCode updatePrice(User user,Good good) throws Exception;
    
    public void xx(User user,String...numIids);
    
    /**
     * 处理错误信息
     * @param good	商品
     * @param task	任务
     * @param responseMessage 封装好的响应错误信息
     * @return
     */
    public ResponseDealResultCode dealErrorMessage(Good good,Task task ,ResponseMessage responseMessage);
    
    /**
     * 返回处理商品响应代码
     * @return
     */
    public ResponseDealResultCode getResponseDealResultCode(ResponseCode response);
    /**
     * 记录任务日志
     * @param task
     * @param good
     * @param responseMessage
     */
    public void logErrorDealGood(Task task,Good good,ResponseMessage responseMessage);
}



