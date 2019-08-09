package com.youmeng.taoshelf.web;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youmeng.taoshelf.entity.Tasklog;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.mapper.TasklogMapper;
import com.youmeng.taoshelf.service.UserService;

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
	private TasklogMapper taskLogMapper;
	@Autowired
	private UserService userService;
	/**
	 * 获取任务日志
	 * @param limit 分页限制
	 * @param current	当前页码
	 * @param taskId	任务ID
	 * @param field		排序字段
	 * @param order		排序
	 * @return
	 */
	@RequestMapping("/getTaskLogByTaskId")
	public String getTaskLogByTaskId(@RequestParam int limit,@RequestParam int current,@RequestParam String taskId
			,@RequestParam(required=false)String field
			,@RequestParam(required=false)String order
			,HttpSession session){

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
		
		return jsonObject.toJSONString();
	}
	/**
	 * 所有任务日志页面
	 * @param session
	 * @return
	 */
	@RequestMapping("/tasklog")
	public ModelAndView tasklog(HttpSession session){
		User user = userService.getUserByNick((String) session.getAttribute("nick"));
		ModelAndView mav = new ModelAndView("user/taskLog");
		mav.addObject("user", user);
		return mav;
	}
	/**
	 * 指定任务ID的任务日志页面
	 * @param session
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/getTaskLogByTaskIdPage")
	public ModelAndView getTaskLogByTaskIdPage(HttpSession session,@RequestParam String taskId){
		User user = userService.getUserByNick((String) session.getAttribute("nick"));
		ModelAndView mav = new ModelAndView("user/taskLog");
		mav.addObject("user", user);
		mav.addObject("taskId",taskId);
		return mav;
	
	}
}

