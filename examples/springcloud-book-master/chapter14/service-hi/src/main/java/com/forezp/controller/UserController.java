package com.forezp.controller;


import com.forezp.domain.User;
import com.forezp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@RequestMapping(value = "/registry",method = RequestMethod.POST)
	public User createUser( @RequestParam("username") String username
	, @RequestParam("password") String password) {
		return userService.create(username,password);
	}
	
	@RequestMapping("/t1")
	public String t1(){
		return "不需要授权";
	}
	
	@RequestMapping("/t2")
	public String t2(){
		return "需要授权";
	}

}
