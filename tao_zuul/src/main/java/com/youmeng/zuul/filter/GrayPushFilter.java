package com.youmeng.zuul.filter;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.youmeng.common.support.RibbonFilterContextHolder;
import com.youmeng.zuul.conf.BasicConf;

/**
 * 灰度发布过滤器
 * Hystrix ThreadLocal数据丢失问题解决
 **/
public class GrayPushFilter extends ZuulFilter {
   @Autowired
   private BasicConf basicConf;

   public GrayPushFilter() {
       super();
   }

   @Override
   public boolean shouldFilter() {
       RequestContext ctx = RequestContext.getCurrentContext();
       Object success = ctx.get("isSuccess");
       return success == null ? true : Boolean.parseBoolean(success.toString());
   }

   @Override
   public String filterType() {
       return "route";
   }

   @Override
   public int filterOrder() {
       return 6;
   }
   private static AtomicInteger ac = new AtomicInteger();
   @Override
   public Object run() {
       RequestContext ctx = RequestContext.getCurrentContext();
       // AuthFilter验证成功之后设置的用户编号
       String loginUserId = ctx.getZuulRequestHeaders().get("uid");
       RibbonFilterContextHolder.getCurrentContext().add("userId", loginUserId);
       // 灰度发布的服务信息
       System.err.println("HHH:"+basicConf.getGrayPushServers());
       //RibbonFilterContextHolder.getCurrentContext().add("servers",ac.addAndGet(1)+"");
       RibbonFilterContextHolder.getCurrentContext().add("servers",basicConf.getGrayPushServers());
       // 灰度发布的用户ID信息
       RibbonFilterContextHolder.getCurrentContext().add("userIds", basicConf.getGrayPushUsers());
       return null;
   }
}