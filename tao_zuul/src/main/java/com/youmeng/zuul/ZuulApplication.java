package com.youmeng.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy		//自带@EnableDiscoveryClient  等于是自带了eureka客户端
@EnableEurekaClient
public class ZuulApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ZuulApplication.class, args);
	}

}
