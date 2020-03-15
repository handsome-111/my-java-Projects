package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

// <1>
@SpringBootApplication
@EnableConfigServer // 该注解会安装Spring Cloud Config Server
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
	}
	
}
