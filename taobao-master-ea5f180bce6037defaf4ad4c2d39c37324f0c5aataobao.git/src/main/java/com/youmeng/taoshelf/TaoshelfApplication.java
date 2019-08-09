package com.youmeng.taoshelf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
public class TaoshelfApplication {
    public static void main(String[] args) {
    	System.out.println(2);
        SpringApplication.run(TaoshelfApplication.class, args);
    }

} 
