package com.study.springboottesting;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
@SpringBootTest(classes = SpringbootTestingApplication.class) 	//// 指定启动类
@TestPropertySource("classpath:application.properties")
@AutoConfigureWebTestClient
class SpringbootTestingApplicationTests {

	private Logger logger = LoggerFactory.getLogger(SpringbootTestingApplicationTests.class);

		
    @Test
    void exampleTest(@Autowired WebTestClient webClient) {
        webClient.get().uri("/").exchange().expectStatus().isOk().expectBody(String.class).isEqualTo("Hello World");
    }



}
