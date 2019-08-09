package com.youmeng.mq.receiver.handler;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.youmeng.feign.service.mq.message.Killer;
@Component
public class MessageReceiver {
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	/**
	 * 收到的消息自动序列化为Killer对象,如果发送的是其他对象,则会报错
	 * @param killer
	 */
	@RabbitListener(queues="send")			//监听的队列,可以监听多个队列,接受多个队列的消息
	public void process(Killer killer){
		System.out.println("killerMessage:" + killer);
	}
	
	/**
	 * Message对象是spring 封装的对象，即接收消息的默认对象
	 * @param message
	 */
	@RabbitListener(queues="send")			//监听的队列,可以监听多个队列,接受多个队列的消息
	public void process2(Message message){
		Object object = message.getBody();		//返回字节数组
		MessageProperties properties = message.getMessageProperties();
		System.out.println("Message:" + message + "killerMessage:" + object + ",propertis:"+properties);
	}
	
	@RabbitListener(queues="send2")		//监听的队列
	public void process3(String message){
		/*Killer killer=(Killer) amqpTemplate.receiveAndConvert("send");
		if(killer!=null){
			System.out.println("receive:"+killer);
		}*/
		//String message = (String) amqpTemplate.receiveAndConvert();
		System.out.println("message:" + message);
	}
}
	
