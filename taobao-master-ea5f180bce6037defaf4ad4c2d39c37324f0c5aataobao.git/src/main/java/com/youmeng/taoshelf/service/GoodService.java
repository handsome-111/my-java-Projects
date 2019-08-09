package com.youmeng.taoshelf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItemUpdateDelistingRequest;
import com.taobao.api.request.ItemUpdateListingRequest;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.youmeng.taoshelf.entity.Good;
import com.youmeng.taoshelf.entity.Result;
import com.youmeng.taoshelf.entity.User;

@Service
public class GoodService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "client1")
    private TaobaoClient client1;

    @Resource(name = "client2")
    private TaobaoClient client2;

    @Resource
    private LogService logService;
    
    //获取在售
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
            Long total_results = JSON.parseObject(response.getBody()).getJSONObject("items_onsale_get_response").getLong("total_results");
            List<Good> goodList = new ArrayList<>();
            if (total_results > 0) {
                JSONArray jsonArray = JSON.parseObject(response.getBody()).getJSONObject("items_onsale_get_response").getJSONObject("items").getJSONArray("item");
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
            List<Good> goodList = new ArrayList<>();
            if (total_results > 0) {
                JSONArray jsonArray = JSON.parseObject(response.getBody()).getJSONObject("items_inventory_get_response").getJSONObject("items").getJSONArray("item");
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
    public ResponseCode doGoodListing(User user, Good good) {
    	/**
    	 * 发送请求
    	 */
        ItemUpdateListingRequest request = new ItemUpdateListingRequest();
        request.setNumIid(good.getNumIid());
        request.setNum(good.getNum());
        ItemUpdateListingResponse response;
        try {
            response = client1.execute(request, user.getSessionKey1());
        	//response = client1.execute(request, user.getSessionKey1());
            /**
             * 正常响应
             */
            if (response.getBody().contains("item_update_listing_response")) {
                good.setApproveStatus("onsale");
                return ResponseCode.ITEM_UPDATE_LISTING_RESPONSE;
            } 
            /**
             * 异常响应
             */
            String subCode = response.getSubCode();
            //logger.info("调用上架接口：错误代码:"+subCode + ",错误信息"+response.getSubMsg() + ",错误信息2：" + response.getMessage());
            for(ResponseCode responseCode : ResponseCode.values()){
            	if(responseCode.equals(subCode)){
            		return responseCode;
            	}
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return ResponseCode.ERROR_CODE;
    }

    //下架
    public ResponseCode doGoodDelisting(User user, Good good) {
    	/**
    	 * 发送请求
    	 */
        ItemUpdateDelistingRequest request = new ItemUpdateDelistingRequest();
        request.setNumIid(good.getNumIid());
        ItemUpdateDelistingResponse response;
        try {
            response = client1.execute(request, user.getSessionKey1());
            /**
             * 正常响应
             */
            if (response.getBody().contains("item_update_delisting_response")) {
                good.setApproveStatus("instock");
                return ResponseCode.ITEM_UPDATE_LISTING_RESPONSE;
            } 
            /**
             * 异常响应
             */
            String subCode = response.getSubCode();
            //logger.info("调用下架接口：错误代码:"+subCode + ",错误信息"+response.getSubMsg() + ",错误信息2：" + response.getMessage());
            for(ResponseCode responseCode : ResponseCode.values()){
            	if(responseCode.equals(subCode)){
            		return responseCode;
            	}
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return ResponseCode.ERROR_CODE;		//响应其他错误
    }
	
}
