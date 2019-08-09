package com.youmeng.taoshelf.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 调用淘宝接口响应代码，主要获取淘宝最新的错误信息来及时更新应用 前端控制器
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-07
 */
@Controller
@RequestMapping("/responseMessage")
public class ResponseMessageController {
	public ModelAndView task(){
		return null;
	}
}

