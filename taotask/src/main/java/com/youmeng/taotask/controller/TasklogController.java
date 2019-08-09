package com.youmeng.taotask.controller;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmeng.common.base.taotask.entity.Task;
import com.youmeng.common.base.taotask.entity.Tasklog;
import com.youmeng.taotask.mapper.TasklogMapper;
import com.youmeng.taotask.service.TasklogService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-06
 */
@RestController
@RequestMapping("/tasklog")
public class TasklogController {
	@Autowired
	private TasklogService tasklogService;
	
	@RequestMapping("/t1")
	public String test(){
		Task task = new Task();
		task.setId(111+ "qweqweqwe");
		Tasklog tasklog = new Tasklog();
		tasklog.setGoodid(111 + "");
    	tasklog.setTask(task);
    	tasklog.setTime(new Date());
    	tasklog.setMsg("流量不足，任务停止");
    	tasklog.setTitle("XX自慰器");
		return tasklogService.save(tasklog) + "xx";
	}
	
	@RequestMapping("/t2")
	public String test2(){
		Task task = new Task();
		task.setId("xxxxxqweqweqwe");
		Tasklog tasklog = new Tasklog();
		tasklog.setGoodid("asdasdasasdasdadasdasdsa");
    	tasklog.setTask(task);
    	tasklog.setTime(new Date());
    	tasklog.setMsg("流量不足，任务停止");
    	tasklog.setTitle("XX自慰器");
		return tasklogService.insertTasklog(tasklog) + "x";
	}
}






