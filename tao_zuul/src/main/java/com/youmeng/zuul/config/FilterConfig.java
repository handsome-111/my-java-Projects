package com.youmeng.zuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.youmeng.zuul.filter.AuthFilter;
import com.youmeng.zuul.filter.AuthHeaderFilter;
import com.youmeng.zuul.filter.DebugRequest;
import com.youmeng.zuul.filter.DownGradeFilter;
import com.youmeng.zuul.filter.ErrorFilter;
import com.youmeng.zuul.filter.GrayPushFilter;
import com.youmeng.zuul.filter.IpFilter;
import com.youmeng.zuul.filter.LimitFilter;
/**
 * 配置过滤器
 * 修改zuul请求debug日志输出类，获取post请求参数
 * @author Administrator
 *
 */
@Configuration
public class FilterConfig {

    @Bean
    public AuthHeaderFilter preRequestLogFilter() {
        return new AuthHeaderFilter();
    }

    /*@Bean
    public LimitFilter limitFilter() {
        return new LimitFilter();
    }*/

    /*@Bean
    public DownGradeFilter downgradeFilter() {
        return new DownGradeFilter();
    }*/

    /*@Bean
    public IpFilter ipFilter() {
        return new IpFilter();
    }*/

    /*@Bean
    public GrayPushFilter grayPushFilter() {
        return new GrayPushFilter();
    }*/

    /*@Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }*/

    /*@Bean
    public DebugRequest debugRequest() {
        return new DebugRequest();
    }*/ 
    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }
}