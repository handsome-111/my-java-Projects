package com.youmeng.zuul.filter;

import com.google.common.net.HttpHeaders;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 调用服务前添加认证请求头过滤器
 *
 **/
public class AuthHeaderFilter extends ZuulFilter {

    public AuthHeaderFilter() {
        super();
    }

    /**
     * 是否开启过滤器
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Object success = ctx.get("isSuccess");
        return success == null ? true : Boolean.parseBoolean(success.toString());
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        /**
         * 1.在ZUUL中添加请求头，即在路有前添加请求头
         * 2.从静态变量里获取属性fangjia.auth.token
         */
        ctx.addZuulRequestHeader("auth", System.getProperty("wuhongjian.auth.token") + "111");
        ctx.getRequest().setAttribute("aa", "bb");
        ctx.addZuulRequestHeader("aaa", "ccc");
        System.out.println("添加请求头");
        return null;
    }
}