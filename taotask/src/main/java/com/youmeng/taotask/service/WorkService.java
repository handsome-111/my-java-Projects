package com.youmeng.taotask.service;

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

public interface WorkService {
    /**
     * 获取商品总数
     * @param type
     * @return
     */
    public long getGoodsCount(User user,String type);
    
    /**
     * 获取所有的商品，根据类目进行分类
     * @param user
     * @param type
     * @param page
     * @return
     */
    public Map<Integer,List<Good>> getGoodsByCategory(User user,String type,long page);
    
    /**
     * 获取商品并存放于队列中
     * @param user
     * @param type
     * @param page
     * @return
     */
    public BlockingQueue<Good> getGoodsByQueue(User user,String type,long page);
}
