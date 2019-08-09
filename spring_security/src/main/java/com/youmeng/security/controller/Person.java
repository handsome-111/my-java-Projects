package com.youmeng.security.controller;

import java.util.Date;

public class Person {
	private String name;
	private String password;
	private int age;
	private Date time;
	
	public Person(String name, String password, int age, Date time) {
		super();
		this.name = name;
		this.password = password;
		this.age = age;
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", password=" + password + ", age=" + age + ", time=" + time + "]";
	}
	
}
