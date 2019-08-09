package com.youmeng.taoshelf.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.youmeng.taoshelf.entity.PageInfo;
import com.youmeng.taoshelf.entity.Task;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.service.TaskService;
import com.youmeng.taoshelf.service.UserService;


@Controller
public class TaskController {
    @Resource
    private TaskService taskService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Resource
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired 
    private RestTemplate restTemplate;

    @RequestMapping("/task")
    public ModelAndView task(ModelMap modelMap, HttpSession session,
                             @RequestParam(defaultValue = "1") int page_no,
                             @RequestParam(defaultValue = "8") int page_size) {
        ModelAndView modelAndView = new ModelAndView("/user/task");
        User user = userService.getUserByNick((String) session.getAttribute("nick"));
        modelAndView.addObject("user", user);
        Page<Task> taskPage = taskService.getTasksByUser(user, page_no - 1, page_size);
        modelAndView.addObject("task_total", taskPage.getTotalElements());
        modelAndView.addObject("page_size", page_size);
        modelAndView.addObject("page_no", page_no);
        List<Task> tasks = taskPage.getContent();
        for (Task task : tasks) {
            if (task.getStatus().equals("正在执行任务")) {
                //String count = redisTemplate.opsForValue().get(task.getId());
                /*if (task.getEndTime() == null) {
                    task.setStatus(task.getStatus() + count + "/" + task.getNum());
                } else {
                    //task.setStatus(task.getStatus() + "(第" + count + "次上下架)");
                	task.setStatus(task.getStatus());
                }*/
            }
        }
        modelAndView.addObject("tasks", tasks);
        if (modelMap.containsAttribute("info")) {
            PageInfo info = (PageInfo) modelMap.get("info");
            modelAndView.addObject("info", info);
        }
        return modelAndView;
    }

    @RequestMapping("/task_add/{taskType}")
    public ModelAndView taskAdd(HttpSession session,@PathVariable String taskType){
    	 ModelAndView modelAndView = new ModelAndView("/user/" + taskType);
         User user = userService.getUserByNick((String) session.getAttribute("nick"));
         modelAndView.addObject("user", user);
         return modelAndView;
    }

    @PostMapping("/add_task1")
    public String addTask1(HttpSession session, RedirectAttributes attributes,
                           @RequestParam String type,
                           @RequestParam String start,
                           @RequestParam String end) throws ParseException {
        User user = userService.getUserByNick((String) session.getAttribute("nick"));
        Task task = new Task();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = dateFormat.parse(start);
        Date endTime = dateFormat.parse(end);
        task.setType(type);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setUser(user);
        task.setTaskType(1);
        attributes.addFlashAttribute("info", taskService.addTask(task));
        return "redirect:/task";
    }

    @PostMapping("/add_task2")
    public String addTask2(HttpSession session, RedirectAttributes attributes,
                           @RequestParam String type,
                           @RequestParam String start) throws ParseException {
        User user = userService.getUserByNick((String) session.getAttribute("nick"));
        Task task = new Task();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = dateFormat.parse(start);
        task.setType(type);
        task.setStartTime(startTime);
        task.setUser(user);
        task.setTaskType(2);
        attributes.addFlashAttribute("info", taskService.addTask(task));
        return "redirect:/task";
    }

    /**
     * 完整上架任务
     * @param session
     * @param attributes
     * @param type
     * @param start
     * @return
     * @throws Exception
     */
    @PostMapping("/startGoodsListingTask")
    public String addListingTask(HttpSession session, RedirectAttributes attributes, @RequestParam String type, @RequestParam String start)throws Exception{
    	User user = userService.getUserByNick((String) session.getAttribute("nick"));
        Task task = new Task();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = dateFormat.parse(start);
        task.setType(type);
        task.setStartTime(startTime);
        task.setUser(user);
        task.setTaskType(3);
        attributes.addFlashAttribute("info", taskService.goodListingTask(task));
		return "redirect:/task";
    }
    
    /**
     * 完整下架任务
     * @param session
     * @param attributes
     * @param type
     * @param start
     * @return
     * @throws Exception
     */
    @PostMapping("/startGoodsDelistingTask")
    public String addDelistingTask(HttpSession session, RedirectAttributes attributes, @RequestParam String type, @RequestParam String start)throws Exception{
    	User user = userService.getUserByNick((String) session.getAttribute("nick"));
        Task task = new Task();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = dateFormat.parse(start);
        task.setType(type);
        task.setStartTime(startTime);
        task.setUser(user);
        task.setTaskType(4);
        attributes.addFlashAttribute("info", taskService.goodDelistingTask(task));
		return "redirect:/task";
    }
    
    
    @GetMapping("/remove_task")
    public String removeTask(HttpSession session, RedirectAttributes attributes, @RequestParam String id) {
        User user = userService.getUserByNick((String) session.getAttribute("nick"));
        attributes.addFlashAttribute("info", taskService.removeTaskById(user, id));
        return "redirect:/task";
    }

    @RequestMapping("/stop_task")
    @ResponseBody
    public String stopTask(@RequestParam String id) {
    	String number = redisTemplate.opsForValue().get(id);
        return taskService.stopTaskById(id,number);
    }
    
    /**
     * 停止所有的任务
     * redis:
     * key:stopTasks   value:所有停止的任务
     * key:allTaskStopFlag    value:1
     * @return
     */
    @RequestMapping("/8088/stop_all_task")
    @ResponseBody
    public String stopAllTask(){
    	Object allTaskStopFlag = redisTemplate.opsForValue().get("allTaskStopFlag");		//任务停止标记
    	logger.info("准备停止所有任务" + "all:" + allTaskStopFlag);
    	/**
    	 * 如果已经开启了停止任务，则返回页面
    	 */
    	if(allTaskStopFlag != null){
        	return "任务已停止，请勿重复停止";
    	}
    	/**
    	 * 开启停止任务
    	 */
    	Set<String> keys = (Set<String>) redisTemplate.keys("task*");
    	Iterator<String> ite = keys.iterator();
    	/**
    	 * 迭代所有正在执行任务的key
    	 */
    	while(ite.hasNext()){
    		String key = (String) ite.next();
    		String taskId = key.substring(4);
    		Task task = taskService.getTaskById(taskId);
    		/**
    		 * 任务不存在/已删除    删除任务
    		 */
    		if(task == null){
        		redisTemplate.delete(key);
    			continue;	//下一个任务
    		}
    		redisTemplate.opsForSet().add("stopTasks", taskId);

    		/**
    		 * 设置结束时间在内存中
    		 */ 
    		redisTemplate.opsForValue().set(taskId, task.getEndTime().getTime() + "");
    		/**
    		 * 停止任务
    		 */
    		redisTemplate.delete(key);
    	}
    	/**
    	 * 设置停止任务标志
    	 */
    	redisTemplate.opsForValue().set("allTaskStopFlag", "true");
    	logger.info("停止的任务:" + redisTemplate.opsForSet().members("stopTasks"));
    	return "成功停止任务:";
    }
    /**
     * 删除指定的key
     * @param key
     * @return
     */
    //8088/removeKey?key=allTaskStopFlag
    @RequestMapping("/8088/removeKey")
    @ResponseBody
    public String removeKey(@RequestParam String key){
    	redisTemplate.delete(key);
    	return "删除key： " + key +"成功";
    }
    /**
     * 获取所有正在运行的任务
     * @return
     */
    @RequestMapping("/8088/getAllRunningTask")
    @ResponseBody
    public String getAllRunningTask(){
    	Set<String> keys = (Set<String>) redisTemplate.keys("task*");
    	return keys.toString();
    }
    
    /**
     * 开启停止了的任务
     */
    @RequestMapping("/8088/start_stoped_task")
    @ResponseBody
    public String startStopedTask(){
    	redisTemplate.delete("allTaskStopFlag");		//删除停止标记
    	Set<String> tasks = redisTemplate.opsForSet().members("stopTasks");	//关闭的任务
    	logger.info("获取到的全部taskId:" + tasks);
    	Iterator<String> ite = tasks.iterator();
    	Iterator<String> ite2 = tasks.iterator();
    	/**
    	 * 设置结束时间 
    	 * 并且判断所有任务是否已全部关闭
    	 */
    	while(ite2.hasNext()){
    		String taskId = ite2.next();
    		Task task = taskService.getTaskById(taskId);
    		/**
    		 * 判断任务是否存在，不存在则删除
    		 */
    		if(task == null){
				redisTemplate.delete("task" + taskId);
    			continue;
    		}
    		String end_time = redisTemplate.opsForValue().get(taskId);
    		if(end_time == null){
    			continue;
    		}
    		/**
    		 * 设置任务结束时间
    		 */
    		Date endTime = new Date(Long.parseLong(redisTemplate.opsForValue().get(taskId)));
    		task.setEndTime(endTime);
    		taskService.saveTask(task);
    		redisTemplate.delete(taskId);
    		
    		if(!"任务结束".equals(task.getStatus())){
    			return "任务还未结束完,请等待任务全部结束再开始";
    		}
    	}
    	
    	/**
    	 * 开始任务
    	 */
    	outerWhile:
    	while(ite.hasNext()){
    		String taskId = ite.next();
    		Task task = taskService.getTaskById(taskId);
    		logger.info("task:" + task);
    		if(task != null){
    			/**
    			 * 如果当前时间在结束时间之后，结束并跳到下一个任务
    			 */
    			if(new Date().after(task.getEndTime())){
    				logger.info("这个任务时间过期，跳过:" + task);
    				
    				continue outerWhile;					
    			}
    			logger.info("任务没有过期，开搞");
    			int type = task.getTaskType();
				/**
				 * 参数
				 */
    			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
				paramMap.add("taskId", taskId);

    			switch(type){
    				//完整上架
    				case 3:{
	    						String response = restTemplate.postForObject("http://localhost:81/taotask/startGoodsListingTask", paramMap, String.class);
	        				    System.out.println("发起调用：" + response);
    				    break;
    				}
    				//完整下架
    				case 4:{
		    				    String response = restTemplate.postForObject("http://localhost:81/taotask/startGoodsDeListingTask", paramMap, String.class);
	        				    System.out.println("发起调用：" + response);
    				    break;
    				}
    				//上下架
    				default:{
		    				    String response = restTemplate.postForObject("http://localhost:81/taotask/start", paramMap, String.class);
	        				    System.out.println("发起调用：" + response);
    				    break;
    				}
    			}
    		}
    	}
    	redisTemplate.delete("stopTasks");
    	redisTemplate.delete("allTaskStopFlag");
    	return "任务已全部启动";
    }
}






