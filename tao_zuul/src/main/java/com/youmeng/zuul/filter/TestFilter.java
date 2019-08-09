package com.youmeng.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class TestFilter extends ZuulFilter{

	/**
	 * 执行自己业务的逻辑，直接返回null即可
	 * ctx为请求上下文，可以获取响应等一系列的信息
	 */
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		System.out.println(ctx + "------------------," + ctx.getRequest().getContextPath());
		return null;
	}
	/**
	 * 是否执行过滤器，返回true表示执行，false不执行
	 */
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		Object success = ctx.get("isSuccess");
		return success == null ? true : Boolean.parseBoolean(success.toString());
	}
	/**
	 * 过滤器的执行顺序，数值越小，优先级越高
	 */
	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * 过滤器类型：pre,route,post,error
	 */
	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

}
