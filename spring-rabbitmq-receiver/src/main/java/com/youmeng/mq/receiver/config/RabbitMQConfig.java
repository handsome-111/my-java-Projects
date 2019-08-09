package com.youmeng.mq.receiver.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig{
	/**
	*配置队列相关
	*/
	@Bean
	public Queue newQueue(){
		return new Queue("send");
	}

	@Bean
	public Queue newQueue2(){
		return new Queue("send2");
	}

	/**
	*配置交换器相关
	*/
	@Bean
	public Exchange newDirectExchange(){
		return new DirectExchange("directExchange",true,true);
	}

	@Bean
	public Exchange newTopicExchange(){
		return new TopicExchange("topicExchange",true,true);
	}
   
	@Bean
	public Exchange newFanoutExchange(){
		return new FanoutExchange("fanoutExchange",true,true);
	}

	/**
	*配置队列和交换器绑定
	*/
	@Bean
	public Binding newDirectBinding(){
		return BindingBuilder.bind(newQueue()).to(newDirectExchange()).with("send").noargs();
	}
   
	@Bean
	public Binding newDirectBinding1(){
		return BindingBuilder.bind(newQueue2()).to(newDirectExchange()).with("send2").noargs();
	}

}