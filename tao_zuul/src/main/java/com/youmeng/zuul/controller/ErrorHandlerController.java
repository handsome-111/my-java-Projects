package com.youmeng.zuul.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RestController
public class ErrorHandlerController implements ErrorController{
	@Autowired
	private ErrorAttributes errorAttributes;
	
	@Override
	public String getErrorPath() {
		return "/error";
	}
	@RequestMapping("/error")
	public ResponseData error(HttpServletRequest request){
		System.out.println("-------------------");
		Map<String,Object> errorAttributes = getErrorAttributes(request);
		String message = (String) errorAttributes.get("message");
		String trace = (String) errorAttributes.get("trace");
		if(StringUtils.isNotBlank(trace)){
			message += String.format(" and trace %s", trace);
		}
		return ResponseData.getInstance(message);
	}
	private Map<String,Object> getErrorAttributes(HttpServletRequest request){
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		return errorAttributes.getErrorAttributes((WebRequest) requestAttributes, true);
	}
}
