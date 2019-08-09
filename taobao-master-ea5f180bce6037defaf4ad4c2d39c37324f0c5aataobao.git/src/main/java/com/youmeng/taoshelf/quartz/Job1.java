package com.youmeng.taoshelf.quartz;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.youmeng.taoshelf.entity.Good;
import com.youmeng.taoshelf.entity.Result;
import com.youmeng.taoshelf.entity.Task;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.quartz.task.MainTask;
import com.youmeng.taoshelf.quartz.task.NextMainWork;
import com.youmeng.taoshelf.service.GoodService;
import com.youmeng.taoshelf.service.LogService;
import com.youmeng.taoshelf.service.TaskService;

public class Job1 extends QuartzJobBean {
	
	public static ExecutorService exec = Executors.newCachedThreadPool();
    public static Map<String,Thread> secondsThreads = new ConcurrentHashMap<String, Thread>();
    public static Map<String,Thread> firstThreads = new ConcurrentHashMap<String, Thread>();
    @Autowired
    private RestTemplate restTemplate;
	
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private TaskService taskService;

    @Resource
    private GoodService goodService;

    @Resource
    private LogService logService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private User user;

    private String taskId;

    private Task task;

    private long totalNum;

    private Map<Long, String> originalStatus1 = new HashMap<>();

    private Map<Integer,List<Good>> acquiredGoods = new HashMap<Integer,List<Good>>();
    
    public static Map<String,MainTask> jobs = new ConcurrentHashMap<String,MainTask>();		//map<taskId,任务>
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        //从context中获取task_id、user
        taskId = jobDataMap.getString("task_id");
        /*redisTemplate.opsForValue().set(taskId, "0");
        task = taskService.getTaskById(taskId);
        user = task.getUser();*/
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
		paramMap.add("taskId", taskId);
	    String response = restTemplate.postForObject("http://localhost:81/taotask/start", paramMap, String.class);
	    System.out.println(response);
        //initTask();
    }
    
    

    //初始化任务
    private void initTask() { 
    	logger.info("用户的两个sessionkey:" + user.getSessionKey1() + "," + user.getSessionKey2());
    	String type = task.getType();
        /**
         * 录入任务
         */
        logService.log(user, task.getDescription() + "任务进度", "任务开始");
        totalNum = readTotalNum(task.getType());
        logService.log(user, task.getDescription() + "任务进度", "任务总数" + totalNum + "件");
        task.setNum(totalNum);
        task.setStatus("正在读取商品列表");
        taskService.saveTask(task);
        task = taskService.getTaskById(taskId);
        /**
         * 获取分页
         */
        long page = 1;
        if(totalNum % 200 == 0){
        	page = totalNum / 200;
        }else{
        	page = totalNum / 200 + 1;
        }
        
    	acquiredGoods = readGoodList(acquiredGoods,page,task.getType());		//读取商品
        MainTask work = new MainTask(user,type,acquiredGoods,task);
        work.setGoodService(goodService);
        work.setLogService(logService);
        work.setRedisTemplate(redisTemplate);
        work.setTaskService(taskService);
        Job1.jobs.put(task.getId(),work);
		Job1.firstThreads.put(task.getId(), Thread.currentThread());

        
        if(totalNum == 0){
        	work.finishTask();
        	logger.info("没有商品无法上下架");
        	return;
        }
        
        /**
         * 子任务
         * 1、读商品列表
         * 2、对商品列表中的商品进行循环上下架
         * 3、任务结束后、对商品列表里的商品进行恢复初始状态
         */
        try {
        	logService.log(user, task.getDescription() + "任务进度", "任务开始执行");
        	/**
        	 * 开启执行上下架的第二步任务
        	 */
        	NextMainWork executeTask = new NextMainWork(work);
        	executeTask.setGoodService(goodService);
            exec.execute(executeTask);
        	//第一步任务
        	work.cycleWork();
		} catch (Exception e) {
            e.printStackTrace();
            logService.log(user, task.getDescription() + "任务进度", "任务异常中止");
            work.getRecoverGoods();
        }
    }

    //读总数(单线程)
    private long readTotalNum(String type) {
    	long totalNum = 0;
        switch (type) {
            case "仓库商品": {
                Result<Good> result = goodService.getGoodsInstock(user, null, 5L, 1L);
                totalNum = result.getTotal();
                break;
            }
            case "在售商品": {
                Result<Good> result = goodService.getGoodsOnsale(user, null, 5L, 1L);
                totalNum = result.getTotal();
                break;
            }
        }
        return totalNum;
    }

    //读列表(双线程)
    private Map<Integer,List<Good>> readGoodList(Map<Integer,List<Good>> goods, long page, String type) {
    	Result<Good> result = new Result<Good>();
        switch (type) {
            case "仓库商品": {
                for (long i = 1; i <= page; i++) {
                    Result<Good> temp = goodService.getGoodsInstock(user, null, 200L, i);
                    List<Good> listGoods = temp.getItems();
                    result.append(listGoods);
                }
                break;
            }
            case "在售商品": {
                for (long i = 1; i <= page; i++) {
                	Result<Good> temp = goodService.getGoodsOnsale(user, null, 200L, i);
                    List<Good> listGoods = temp.getItems();
                    result.append(listGoods);
                }
                break;
            }
        }
        logger.info("真正获取商品的总数为：" + result.getTotal());
        goods = result.getMap();		//得到分类的结果集
        int size = 0;
        
        
    	Set<Integer> keys = goods.keySet();
    	Iterator<Integer> ite = keys.iterator();
    	while(ite.hasNext()){
    		List<Good> goodList = goods.get(ite.next());
    		
    		size += goodList.size();		//分配的数量
    		
    		for (Good good : goodList) {
                originalStatus1.put(good.getNumIid(), good.getApproveStatus());
            }
    	}
    	
    	
        logService.log(user, task.getDescription() + "任务进度", "子任务"+ "分配" + size + "件");
        task.setStatus("正在执行任务");
        taskService.saveTask(task);
        task = taskService.getTaskById(taskId);
        return goods;
    }
}
