package com.youmeng.security;

import java.util.ArrayList;
import java.util.List;

public class Test {
	private List list = new ArrayList();
	public void tt(Object obj){
		list.add(obj);
		tt(obj);
	}
	
	public static void main(String[] args) {
		new Test().tt("的撒大爱的阿萨大爱的撒的 安德森的撒大爱的阿萨大爱的撒的 安德森 的撒大爱的阿萨大爱的撒的 安德森 的撒大爱的阿萨大爱的撒的 安德森 的撒大爱的阿萨大爱的撒的 安德森 的撒大爱的阿萨大爱的撒的 安德森 的撒大爱的阿萨大爱的撒的 安德森 的撒大爱的阿萨大爱的撒的 安德森 的撒大爱的阿萨大爱的撒的 安德森 的撒大爱的阿萨大爱的撒的 安德森  ");
	}
}
