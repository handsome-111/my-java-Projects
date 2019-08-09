package com.youmeng.taotask.controller;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youmeng.common.base.taotask.entity.ResponseMessage;
import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.common.base.taotask.entity.Tasklog;
import com.youmeng.taotask.mapper.TaskMapper;
import com.youmeng.taotask.mapper.TasklogMapper;
import com.youmeng.taotask.service.ResponseMessageService;
import com.youmeng.taotask.service.TaskService;
import com.youmeng.taotask.work.GoodCycle;
import com.youmeng.taotask.work.GoodDelisting;
import com.youmeng.taotask.work.GoodListing;

/** 
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-06
 */
@RestController
public class TaskController {
	@Autowired
	private TasklogMapper tasklogMapper;
	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private ResponseMessageService responseMessageService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private TasklogMapper taskLogMapper;
	@Autowired
	private LoadBalancerClient loadBalancer; 
	
	@PostMapping("/start") 
	public String start(@RequestParam String taskId){
		new Thread(new Runnable(){
			@Override
			public void run() {
				GoodCycle goodCycle = new GoodCycle(taskId);
				//SimpleGoodCycle sg = new SimpleGoodCycle(taskId);
				goodCycle.start();
			}
		}).start();
		return "the task start success";
	}
	@PostMapping("/startGoodsListingTask")
	public String startGoodsListingTask(@RequestParam String taskId){
		new Thread(new Runnable(){
			@Override
			public void run() {
				GoodListing goodListing = new GoodListing(taskId);
				goodListing.start();
			}
		}).start();
		return "the task start success";
	}
	@PostMapping("/startGoodsDeListingTask")
	public String startGoodsDeListingTask(@RequestParam String taskId){
		new Thread(new Runnable(){
			@Override
			public void run() {
				GoodDelisting goodDelisting = new GoodDelisting(taskId);
				goodDelisting.start();
			}
		}).start();
		
		return "the task start success";
	}
	@RequestMapping("/tt")
	public String test(){
		Tasklog tasklog = new Tasklog();
		
		Task task = new Task();
		task.setId("1");
		
		tasklog.setTime(new Date());
		tasklog.setMsg("错误678");
		tasklog.setTask(task);
		tasklog.setGoodid("qewqe");
		
		int result = tasklogMapper.insertTaskLog(tasklog);
		System.out.println("记录:" + result  );
		return result + "...";
	}
	@RequestMapping("/tt2")
	public String tset2(){
		Task task = taskMapper.getTaskById("1");
		System.out.println(task);
		System.out.println(task.getUser().getNick());
		System.out.println(task.getUser());
		return task.toString();
	}
	@RequestMapping("/tt3")
	public String test3(){
		Task task = taskMapper.getTaskById("1");
		System.out.println(task);
		task.setEndTime(new Date());
		task.setStatus("GG思密达");
		int result = taskMapper.updateTask(task);
		System.out.println(task);
		return "结果:" + result + ",/n" + task;
	}
	@RequestMapping("/tt4")
	public String test4(){
		ResponseMessage rm = new ResponseMessage();
		rm.setCode(530);
		rm.setMsg("错误678");
		rm.setRequestId("12312eqwe");
		rm.setSubCode("7877sub_code");
		rm.setSubMsg("大撒大撒大撒大撒大苏打");
		boolean boo = responseMessageService.save(rm);
		return boo + "";
	}
	/**
	 * 获取任务日志
	 * @param limit 分页限制
	 * @param current	当前页码
	 * @param taskId	任务ID
	 * @param field		排序字段
	 * @param order		排序
	 * @return
	 */
	@RequestMapping("/tt6")
	public String tt6(@RequestParam int limit,@RequestParam int current,@RequestParam String taskId
			,@RequestParam(required=false)String field
			,@RequestParam(required=false)String order){
		System.out.println("limit:" + limit + ",current:" + current + ",field:" + field+",order:" + order);
		Page<Tasklog> page = new Page<Tasklog>();
		page.setSize(limit);
		page.setCurrent(current);
		IPage ip = taskLogMapper.geTasklogByTaskId(page, taskId,order,field);
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
	 * 测试服务
	 * @return
	 */
	@RequestMapping("/tt7")
	public String tt7(){
		return "you son of bitch" + "/n 地址：" + loadBalancer.choose("TAOTASK").getHost() +"端口：" + loadBalancer.choose("TAOTASK").getPort();
	}
	
	@RequestMapping("/tasklog")
	public ModelAndView tasklog(){
		return new ModelAndView("user/taskLog.html");
	}
}

