package com.youmeng.security.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;

@RestController
public class TestController {
	@RequestMapping("/")
	public String x(){
		return "注销";
	}
	@RequestMapping("/a/aa")
	public String a(){
		return "a";
	}
	@RequestMapping("/b/bb")
	public String b(){
		return "b";
	}
	@RequestMapping("/c/c")
	public String c(){
		return "c";
	}
	@RequestMapping("/d/dd")
	public String d(){
		return "d";
	}
	@RequestMapping("/401")
	public String error401(){
		return "401";
	}
	@RequestMapping("/home")
	public ModelAndView home(){
		return new ModelAndView("hello");
	}
	@RequestMapping("/getPerson")
	public String getPerson(String name,int age){
		Person person = new Person(name,"132456",age,new Date());
		System.out.println(person);
		return JSONArray.toJSONString(person);
	}
}
