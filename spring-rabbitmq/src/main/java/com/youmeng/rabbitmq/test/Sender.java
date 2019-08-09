package com.youmeng.rabbitmq.test;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.youmeng.feign.service.mq.message.Killer;


@Component
public class Sender{
	@Autowired
	private AmqpTemplate amqpTemplate;
	public void send(){
		Killer killer = new Killer("Mr.吴",22,"13379959770");
		amqpTemplate.convertAndSend("send",killer);				//将消息发送到路由Key=send的队列中，并发送对象（对象必须实例化Seriable接口，可序列化）
	}
	public void sendString(){
		amqpTemplate.convertAndSend("send2","你好啊，接收者");		//发送字符串到key=send2的队列中
	}
}
