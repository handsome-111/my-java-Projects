package com.study.rabbitmq.example1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Consumer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitmqStudy1Application {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqStudy1Application.class, args);
	}

	@Bean
	public Consumer<Person> log() {
	    return person -> {
	        System.out.println("Received: " + person);
	    };
	}

	public static class Person {
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String toString() {
			return this.name;
		}
	}

}
