package com.youmeng.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringSecurityApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

}
