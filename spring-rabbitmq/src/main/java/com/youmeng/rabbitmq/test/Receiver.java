package com.youmeng.rabbitmq.test;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver{
	/*//@Autowired
	//private AmqpTemplate amqpTemplate;
	@RabbitListener(queues="send")		//监听的队列
	//@RabbitHandler
	public void process(String message){
		String val=(String)amqpTemplate.receiveAndConvert("send");
		if(val!=null){
			System.out.println("receive:"+message);
		}
		System.out.println("receive:"+message);
	}*/
}

   