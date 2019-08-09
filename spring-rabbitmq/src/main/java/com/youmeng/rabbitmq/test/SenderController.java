package com.youmeng.rabbitmq.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SenderController {
	@Autowired
	private Sender sender;
	@RequestMapping("/send")
	public String sendMessage(){
		String message = "你好，son of bitch";
		System.out.println("开始发送消息:" + message);
		sender.send();
		return "消息已发送";
	}
	@RequestMapping("/send1")
	public String send1(){
		sender.sendString();
		return "发送成功";
	}
	
}
