package com.youmeng.eureka_server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
//--spring.profiles.active=eureka-server1
public class EurekaServerApplication {
	public static void main(String[] args) { 
		System.out.println(12);
		new SpringApplicationBuilder(EurekaServerApplication.class).run(args); 
	}
}
