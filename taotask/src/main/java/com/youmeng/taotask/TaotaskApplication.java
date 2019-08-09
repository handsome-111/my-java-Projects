package com.youmeng.taotask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages="com.youmeng.feign.service")
@EnableHystrix
//--spring.profiles.active=taotask1 
public class TaotaskApplication { 
    public static void main(String[] args) {
    	System.out.println("12");
        SpringApplication.run(TaotaskApplication.class, args);
    }
    @Bean(name = "client1")		//如意掌柜APP
    public TaobaoClient getClient_1() {
        return new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "12322527", "db6e35b4a58543e9f48b34419e278377");
    }
    @Bean(name = "client2")		//手机XXAPP
    public TaobaoClient getClient_2() {
        return new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "12402170", "76f407aef5e1f5ad55542b8a5ae63247");
    }
}
