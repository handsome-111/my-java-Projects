package com.youmeng.taotask.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItemPriceUpdateRequest;
import com.taobao.api.request.ItemUpdateDelistingRequest;
import com.taobao.api.request.ItemUpdateListingRequest;
import com.taobao.api.request.ItemUpdateRequest;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.ItemsSellerListGetRequest;
import com.taobao.api.response.ItemPriceUpdateResponse;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.taobao.api.response.ItemUpdateResponse;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.ItemsSellerListGetResponse;
import com.youmeng.common.base.taotask.entity.Good;
import com.youmeng.common.base.taotask.entity.ResponseMessage;
import com.youmeng.common.base.taotask.entity.Result;
import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.common.base.taotask.entity.Tasklog;
import com.youmeng.common.base.taotask.entity.User;
import com.youmeng.common.base.taotask.response.ResponseCode;
import com.youmeng.common.base.taotask.response.ResponseDealResultCode;
import com.youmeng.taotask.mapper.TasklogMapper;
import com.youmeng.taotask.service.GoodService;
import com.youmeng.taotask.service.ResponseMessageService;

@Service
public class GoodServiceImpl implements GoodService{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "client1")
    private TaobaoClient client1;
    
    @Autowired 
    private ResponseMessageService responseMessageService;
    @Autowired
    private TasklogMapper tasklogMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 获取GoodService实例
     * @return
     *//*
    public static GoodService getGoodService(){
    	return SpringContextUtils.getBean(GoodService.class);
    }*/
    
    //获取在售
    @Override
    public Result<Good> getGoodsOnsale(User user, String q, Long page_size, Long page_no) {
        Result<Good> result = new Result<>();
        ItemsOnsaleGetRequest request = new ItemsOnsaleGetRequest();
        request.setFields("num_iid,title,approve_status,num,modified,delist_time,list_time,cid,seller_cids");
        request.setQ(q);
        request.setPageSize(page_size);
        request.setPageNo(page_no);
        ItemsOnsaleGetResponse response;
        try {
            response = client1.execute(request, user.getSessionKey1());
            String body = response.getBody();
            Long total_results = JSON.parseObject(response.getBody()).getJSONObject("items_onsale_get_response").getLong("total_results");
            List<Good> goodList = new ArrayList<>();
            JSONObject items = JSON.parseObject(response.getBody()).getJSONObject("items_onsale_get_response").getJSONObject("items");
            if (total_results > 0 && items!= null) {
                JSONArray jsonArray = items.getJSONArray("item");
                for (Object item : jsonArray) {
                	Good goods = JSON.parseObject(item.toString(), Good.class);
                	goodList.add(goods);
                }
            } 
            result.setTotal(total_results);
            result.setItems(goodList);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    //获取库存
    public Result<Good> getGoodsInstock(User user, String q, Long page_size, Long page_no) {
        Result<Good> result = new Result<>();
        ItemsInventoryGetRequest request = new ItemsInventoryGetRequest();
        request.setFields("num_iid,title,approve_status,num,modified,delist_time,list_time,cid");
        request.setQ(q);
        request.setPageSize(page_size);
        request.setPageNo(page_no);
        ItemsInventoryGetResponse response;
        try {
            response = client1.execute(request, user.getSessionKey1());
            Long total_results = JSON.parseObject(response.getBody()).getJSONObject("items_inventory_get_response").getLong("total_results");
            JSONObject items = JSON.parseObject(response.getBody()).getJSONObject("items_inventory_get_response").getJSONObject("items");
            List<Good> goodList = new ArrayList<>();
            if (total_results > 0 && items!= null) {
                JSONArray jsonArray = items.getJSONArray("item");
                for (Object item : jsonArray) {
	                Good goods = JSON.parseObject(item.toString(), Good.class);
	                goodList.add(goods);
                }
            }
            result.setTotal(total_results);
            result.setItems(goodList);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    
    //上架
    public ResponseDealResultCode doGoodListing(User user, Good good,Task task){
    	/**
    	 * 发送请求
    	 */
        ItemUpdateListingRequest request = new ItemUpdateListingRequest();
        request.setNumIid(good.getNumIid());
        request.setNum(good.getNum());
        ItemUpdateListingResponse response = null;
        try {
			response = client1.execute(request, user.getSessionKey1());
		} catch (ApiException e) {
			e.printStackTrace();
			return ResponseDealResultCode.OUT;
		}
        /**
         * 消耗流量
         */
        consumeFlow(user);
        /**
         * 正常响应
         */
        if (response.getBody().contains("item_update_listing_response")) {
            good.setApproveStatus("onsale");
            return ResponseDealResultCode.SUCCESS;
        } 
        /**
         * 异常响应
         */
        ResponseMessage responseMessage = JSON.parseObject(response.getBody()).getObject("error_response", ResponseMessage.class);
        return dealErrorMessage(good, task, responseMessage);
    }

    //下架
    public ResponseDealResultCode doGoodDelisting(User user, Good good,Task task) {
    	/**
    	 * 发送请求
    	 */
        ItemUpdateDelistingRequest request = new ItemUpdateDelistingRequest();
        request.setNumIid(good.getNumIid());
        ItemUpdateDelistingResponse response = null;
        try {
			response = client1.execute(request, user.getSessionKey1());
		} catch (ApiException e) {
			e.printStackTrace();
			return ResponseDealResultCode.OUT;
		}
        /**
         * 消耗流量
         */
        consumeFlow(user);
        /**
         * 正常响应
         */
        if (response.getBody().contains("item_update_delisting_response")) {
            good.setApproveStatus("onsale");
            return ResponseDealResultCode.SUCCESS;
        } 
        /**
         * 异常响应
         */
        ResponseMessage responseMessage = JSON.parseObject(response.getBody()).getObject("error_response", ResponseMessage.class);
        return dealErrorMessage(good, task, responseMessage);
    }
	
    /**
     * 更新一个商品
     * @return
     */
    public ResponseDealResultCode updateGood(User user,Good good,Task task){
    	ItemUpdateRequest req = new ItemUpdateRequest();
    	String ModifiedState = null;	//更新商品后的状态
    	
    	/**
    	 * 如果为库存商品,则更新为在售商品
    	 */
    	if("instock".equals(good.getApproveStatus())){
        	ModifiedState = "onsale";
    	}else{
        	ModifiedState = "instock";
    	}
    	
    	req.setApproveStatus(ModifiedState);
    	req.setNumIid(good.getNumIid());
		ItemUpdateResponse response = null;
		try {
			response = client1.execute(req, user.getSessionKey1());
		} catch (ApiException e) {
			e.printStackTrace();
			return ResponseDealResultCode.OUT;
		}
		/**
         * 正常响应
         */
        if (response.getBody().contains("item_update_response")) {
            good.setApproveStatus("onsale");
            return ResponseDealResultCode.SUCCESS;
        } 
        /**
         * 异常响应
         */
        ResponseMessage responseMessage = JSON.parseObject(response.getBody()).getObject("error_response", ResponseMessage.class);
        return dealErrorMessage(good, task, responseMessage);
    }
    
    /**
     * 修改一个商品的价格
     */
    public ResponseCode updatePrice(User user,Good good) throws Exception{
    	ItemPriceUpdateRequest req = new ItemPriceUpdateRequest();
    	String ModifiedState = null;	//更新商品后的状态
    	/**
    	 * 如果为库存商品,则更新为在售商品
    	 */
    	if("instock".equals(good.getApproveStatus())){
        	ModifiedState = "onsale";
    	}else{
        	ModifiedState = "instock";
    	}
    	
    	req.setApproveStatus(ModifiedState);
    	req.setNumIid(good.getNumIid());
    	ItemPriceUpdateResponse  rsp = client1.execute(req, user.getSessionKey1());
    	/**
    	 * 正常响应
    	 */
    	if(rsp.getBody().contains("item_update_response")){
    		  good.setApproveStatus(ModifiedState);
              return ResponseCode.SUCCESS;
    	}
    	/**
         * 异常响应
         */
        String subCode = rsp.getSubCode();
        //logger.info("调用修改价格商品接口：错误代码:"+subCode + ",错误信息"+rsp.getSubMsg() + ",错误信息2：" + rsp.getMessage() + ",真错误信息:" + rsp.getBody());
        for(ResponseCode responseCode : ResponseCode.values()){
        	if(responseCode.equals(subCode)){
        		return responseCode;
        	}
        }
        return ResponseCode.ERROR_CODE;		//响应其他错误
    }
    
    public void xx(User user,String...numIids){
    	ItemsSellerListGetRequest req = new ItemsSellerListGetRequest();
    	req.setFields("num_iid,title,nick,approve_status,num,sku,props_name");
    	req.setNumIids("123456,234567");
    	ItemsSellerListGetResponse rsp = null;
		try {
			rsp = client1.execute(req, user.getSessionKey1());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(rsp.getBody());
    }
    
    /**
     * 处理错误信息
     * @param good	商品
     * @param task	任务
     * @param responseMessage 封装好的响应错误信息
     * @return
     */
    public ResponseDealResultCode dealErrorMessage(Good good,Task task ,ResponseMessage responseMessage){
    	ResponseDealResultCode responseDealResultCode = null;
    	/**
    	 * 获取具体的响应结果
    	 */
    	for(ResponseCode responseCode : ResponseCode.values()){
        	if(responseCode.getSub_code().equals(responseMessage.getSubCode())){
        		responseDealResultCode = getResponseDealResultCode(responseCode);
        		break;
        	}
        }
    	/**
    	 * 记录新的日志
    	 */
        if(responseDealResultCode == null){
        	responseDealResultCode = ResponseDealResultCode.NEXT;
        	/**
             * 记录新的错误代码
             */
            responseMessageService.save(responseMessage);
    		this.logErrorDealGood(task, good, responseMessage);
    		return responseDealResultCode;
        }
        /**
    	 * 根据响应结果来记录任务日志
    	 */
    	if(responseDealResultCode == ResponseDealResultCode.NEXT || responseDealResultCode == ResponseDealResultCode.OUT){
    		this.logErrorDealGood(task, good, responseMessage);
    	}
        return responseDealResultCode;
    }
    
    /**
     * 返回处理商品响应代码
     * @return
     */
    public ResponseDealResultCode getResponseDealResultCode(ResponseCode response){
    	ResponseDealResultCode responseDealResultCode = null;
    	switch(response){
	    	case SUCCESS:{
	    		responseDealResultCode =  ResponseDealResultCode.SUCCESS;break;
			}
    		case ERROR_CODE_15_ISP_SYSTEM_BUSY:{
    			responseDealResultCode = ResponseDealResultCode.PRESENT;break;
    		}
    		case ERROR_CODE_50_IC_CHECKSTEP_NO_PERMISSION:{
    			responseDealResultCode = ResponseDealResultCode.OUT;break;
    		}
    		case ERROR_CODE_50_ISV_ITEM_LISTING_SERVICE_ERROR_QUANTITY_ITEM_CAT_TOO_LARGE:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_530_ISV_ERROR_ITEM_IFD_ERROR:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_530_ISV_ERROR_ITEM_QUALIFICATION_CHECK:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_530_ISV_ITEM_UPDATE_SERVICE_ERROR_IC_CATEGORY_CPV_FREEZE:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_530_ISV_ITEM_UPDATE_SERVICE_ERROR_IC_OPTIMISTIC_LOCKING_CONFLICT:{
    			responseDealResultCode = ResponseDealResultCode.PRESENT;break;
    		}
    		case ERROR_CODE_7_LIMITED_BY_API_ACCESS_COUNT:{
    			responseDealResultCode = ResponseDealResultCode.PRESENT;break;
    		}
    		case ERROR_CODE_ERROR_CATEGORY_MARGIN_ISNOT_ENOUGH:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_ISP_TOP_REMOTE_CONNECTION_TIMEOUT:{
    			responseDealResultCode = ResponseDealResultCode.PRESENT;break;
    		}
    		case ERROR_CODE_ERROR_GLOBAL_STOCK_CONTRY_WRONG:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_ERROR_LISTING_FORBIDDEN_QUANTITY_ZERO_ERROR:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_ERROR_LISTING_ITEM_PROPERTIES:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_ERROR_UPDATE_FORBIDDEN_QUANTITY_ZERO_ERROR:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_ERROR_UPDATE_ITEM_PROPERTIES:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_7LIMITED_BY_DYNAMIC_ACCESS_COUNT:{
    			responseDealResultCode = ResponseDealResultCode.PRESENT;break;
    		}
    		case ERROR_CODE_INVALID_NUMIID_OR_IID:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_INVALID_PARAMETER:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    		case ERROR_CODE_LISTING_SERVICE_ERROR_IC_CONCURRENT_MODIFY_FORBIDDEN:{
    			responseDealResultCode = ResponseDealResultCode.PRESENT;break;
    		}
    		case ERROR_CODE_LISTING_SERVICE_ERROR_IC_OPTIMISTIC_LOCKING_CONFLICT:{
    			responseDealResultCode = ResponseDealResultCode.PRESENT;break;
    		}
    		case ERROR_CODE_UPDATE_SERVICE_ERROR_IC_CATEGORY_CP_FREEZE:{
    			responseDealResultCode = ResponseDealResultCode.NEXT;break;
    		}
    	}
    	return responseDealResultCode;
    }
    /**
     * 记录任务日志
     * @param task
     * @param good
     * @param responseMessage
     */
    public void logErrorDealGood(Task task,Good good,ResponseMessage responseMessage){
    	Tasklog tasklog = new Tasklog();
    	tasklog.setGoodid(good.getNumIid() + "");
    	tasklog.setTask(task);
    	tasklog.setTime(new Date());
    	tasklog.setMsg(("(商品上下架异常)") + responseMessage.getSubMsg());
    	tasklog.setTitle(good.getTitle());
    	tasklogMapper.insertTaskLog(tasklog);
    }
    /**
     * 消耗一次流量
     */
    public void consumeFlow(User user){
    	String nick = user.getNick();
    	stringRedisTemplate.opsForValue().decrement("usableFlow" + nick);
    }
}



