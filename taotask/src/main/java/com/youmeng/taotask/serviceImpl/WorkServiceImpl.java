package com.youmeng.taotask.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youmeng.common.base.taotask.entity.Good;
import com.youmeng.common.base.taotask.entity.Result;
import com.youmeng.common.base.taotask.entity.User;
import com.youmeng.taotask.service.GoodService;
import com.youmeng.taotask.service.WorkService;

@Service
public class WorkServiceImpl implements WorkService{
	@Autowired
    private GoodService goodService;
    /**
     * 获取商品总数
     * @param type
     * @return
     */
    public long getGoodsCount(User user,String type){
    	long totalNum = 0;
        switch (type) {
            case "仓库商品": {
                Result<Good> result = goodService.getGoodsInstock(user, null, 5L, 1L);
                totalNum = result.getTotal();
                break;
            }
            case "在售商品": {
                Result<Good> result = goodService.getGoodsOnsale(user, null, 5L, 1L);
                totalNum = result.getTotal();
                break;
            }
        }
        return totalNum;
    }
    
    /**
     * 获取所有的商品，根据类目进行分类
     * @param user
     * @param type
     * @param page
     * @return
     */
    public Map<Integer,List<Good>> getGoodsByCategory(User user,String type,long page){
    	Map<Integer,List<Good>> goods = new HashMap<Integer,List<Good>>();
    	Result<Good> result = new Result<Good>();
        switch (type) {
            case "仓库商品": {
                /*for (long i = 1; i <= page; i++) {
                    Result<Good> temp = goodService.getGoodsInstock(user, null, 200L, i);
                    List<Good> listGoods = temp.getItems();
                    result.append(listGoods);
                }*/
	        	Result<Good> temp = goodService.getGoodsInstock(user, null, 200L, page);
	            List<Good> listGoods = temp.getItems();
	            result.append(listGoods);
                break;
            }
            case "在售商品": {
                /*for (long i = 1; i <= page; i++) {
                	Result<Good> temp = goodService.getGoodsOnsale(user, null, 200L, i);
                    List<Good> listGoods = temp.getItems();
                    result.append(listGoods);
                }*/
            	Result<Good> temp = goodService.getGoodsOnsale(user, null, 200L, page);
                List<Good> listGoods = temp.getItems();
                result.append(listGoods);
                break;
            }
        }
        goods = result.getMap();		//得到分类的结果集
        return goods;
    }
    
    /**
     * 获取商品并存放于队列中
     * @param user
     * @param type
     * @param page 	单页读取
     * @return
     */
    public BlockingQueue<Good> getGoodsByQueue(User user,String type,long page){
    	BlockingQueue<Good> goods = new LinkedBlockingQueue<Good>();
        switch (type) {
            case "仓库商品": {
                /*for (long i = 1; i <= page; i++) {
                    Result<Good> temp = goodService.getGoodsInstock(user, null, 200L, i);
                    goods.addAll(temp.getItems());
                }*/
                Result<Good> temp = goodService.getGoodsInstock(user, null, 200L, page);
                goods.addAll(temp.getItems());
                break;
            }
            case "在售商品": {
                /*for (long i = 1; i <= page; i++) {
                	Result<Good> temp = goodService.getGoodsOnsale(user, null, 200L, i);
                    goods.addAll(temp.getItems());
                }*/
            	Result<Good> temp = goodService.getGoodsOnsale(user, null, 200L, page);
                goods.addAll(temp.getItems());
                break;
            }
        }
        return goods;
    }
}
