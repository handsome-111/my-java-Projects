package com.youmeng.taotask.controller;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youmeng.common.base.taotask.entity.FlowPackage;
import com.youmeng.taotask.service.FlowPackageService;

/**
 * <p>
 * 流量包 前端控制器
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-27
 */
@RestController
@RequestMapping("/flowPackage")
public class FlowPackageController {
	@Autowired
	private FlowPackageService flowPackageService;
	/**
	 * 用户卡包页面
	 * @return
	 */
	@RequestMapping("/flowPackage")
	public ModelAndView flowPackage(){
		return new ModelAndView("user/flow_package.html");
	}
	/**
	 * 生成流量卡包
	 * @return
	 */
	@RequestMapping("/createFlowPackage")
	public String createFlowPackage(@RequestParam long flowMax,@RequestParam double price){
		FlowPackage flowPackage = new FlowPackage();
		flowPackage.setCreateTime(new Date());
		flowPackage.setFlowMax(flowMax);
		flowPackage.setPrice(price);
		
		int result = flowPackageService.insertFlowPackage(flowPackage);
		System.out.println("记录:" + result  );
		return result + "...";
	}
	
	/**
	 * 获取流量包详情
	 * @param limit 分页限制
	 * @param current	当前页码
	 * @param field	排序字段
	 * @param order	排序
	 * @param owner 拥有者
	 * @param user	使用者
	 * @return
	 */
	@GetMapping("/getFlowPackage")
	public String getFlowPackage(@RequestParam int limit,@RequestParam int current
			,@RequestParam(required=false)String field
			,@RequestParam(required=false)String order
			,@RequestParam(required=false)String owner
			,@RequestParam(required=false)String user
			){
		Page<FlowPackage> page = new Page<FlowPackage>();
		page.setSize(limit);
		page.setCurrent(current);
		IPage ip = flowPackageService.getFlowPackage(page, owner, user, order, field);
		/**
		 * 转JSON响应
		 */
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("limit", ip.getSize());
		jsonObject.put("page", ip.getCurrent());
		jsonObject.put("pages", ip.getPages());
		jsonObject.put("count", ip.getTotal());
		jsonObject.put("data", ip.getRecords());
		jsonObject.put("code", 200);
		jsonObject.put("msg", "");
		System.out.println(jsonObject.toJSONString());
		return jsonObject.toJSONString();
	}
	/**
	 * 使用流量包
	 * @param nick	用户
	 * @param flowId	流量包标识
	 * @return
	 */
	@GetMapping("/useFlowPackage")
	public String useFlowPackage(@RequestParam String nick,@RequestParam String flowId){
		return flowPackageService.useingFlowPackage(flowId, nick, new Date());
	}
	/**
	 * 根据密钥获取流量包
	 * @param nick	用户
	 * @param flowId	流量包标识
	 * @return
	 */
	@GetMapping("/getFlowPackageByKey")
	public String getFlowPackageByKey(@RequestParam String nick,@RequestParam String flowId){
		return flowPackageService.getFlowPackageByKey(flowId, nick);
	}
}

