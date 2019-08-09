package com.youmeng.mq.receiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ReceiverApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ReceiverApplication.class, args);
	}
}
