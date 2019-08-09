package com.youmeng.zuul.task;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.youmeng.common.base.ResponseData;
import com.youmeng.feign.service.auth.AuthRemoteClient;
import com.youmeng.feign.service.auth.query.AuthQuery;

/**
 * 定时刷新token
 *
 * @author yinjihuan
 * @create 2017-11-09 15:39
 **/
//@Component
public class TokenScheduledTask {
    private static Logger logger = LoggerFactory.getLogger(TokenScheduledTask.class);

    private final static long SECOND = 1000;
    private final static long MINUTE = 60 * SECOND;
    private final static long HOUR = 60 * MINUTE;
    
    //20小时
    public final static long ONE_Minute = 20 * HOUR;

    
    @Autowired
    private AuthRemoteClient authRemoteClient;

    /**
     * 刷新Token
     */
    @Scheduled(fixedDelay = ONE_Minute)
    public void reloadApiToken() {
        String token = this.getToken();
        while (StringUtils.isBlank(token)) {
            try {
                Thread.sleep(1000);
                token = this.getToken();
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        System.setProperty("wuhongjian.auth.token", token);
    }

    public String getToken() {
        AuthQuery query = new AuthQuery();
        query.setAccessKey("1");
        query.setSecretKey("1");
        ResponseData response = authRemoteClient.auth(query);
        return response.getData() == null ? "" : response.getData().toString();
    }
}