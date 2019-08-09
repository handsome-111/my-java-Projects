package com.youmeng.taotask.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmeng.feign.service.TestService;

@RestController
public class TestController implements TestService{

	@RequestMapping("/test1")
	@Override
	public String test1() {
		return "you son of bitch test1";
	}

	@RequestMapping("/test2")
	@Override
	public String test2() {
		return "you son of bitch test2";
	}

}
