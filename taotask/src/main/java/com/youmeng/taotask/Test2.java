package com.youmeng.taotask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test2 {
	public boolean flag;
	public boolean TaskRunFlag() {
		String flagStr = "1";
		if(flagStr == null || "".equals(flagStr)){
			flag = false;
		}else{
			flag = new Boolean(flagStr);
		}
		return flag;
	}
	public static void main(String[] args) throws Exception {
		System.out.println("/tt");
	}
}
