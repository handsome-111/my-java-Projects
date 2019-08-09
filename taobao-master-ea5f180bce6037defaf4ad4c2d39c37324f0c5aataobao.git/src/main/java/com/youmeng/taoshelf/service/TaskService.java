package com.youmeng.taoshelf.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.youmeng.taoshelf.entity.PageInfo;
import com.youmeng.taoshelf.entity.Task;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.quartz.GoodsDelisting;
import com.youmeng.taoshelf.quartz.GoodsListing;
import com.youmeng.taoshelf.quartz.Job1;
import com.youmeng.taoshelf.quartz.Job2;
import com.youmeng.taoshelf.repository.TaskRepository;


//@Transactional
@Service
public class TaskService {
	@Autowired
	private RedisService redis;
    @Resource
    private Scheduler scheduler;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private TaskRepository taskRepository;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 上架任务
     * @param task
     * @return
     */
    public PageInfo goodListingTask(Task task){
        List<Task> tasks = getTasksByUser(task.getUser());
        for (Task item : tasks) {
            if (item.getStatus().contains("正在执行") || item.getStatus().contains("等待执行")) {
                return new PageInfo("error", "创建任务失败，完整上下架任务时不得有其他正在执行或等待执行的任务");
            }
        }
        if(flowUsage(task.getUser()) == false){
        	return new PageInfo("error","流量不足，请充值后再开启任务");
        }
        User user = task.getUser();
        try {
            taskRepository.save(task);
            String name = task.getId();
            String group = user.getNick();
            JobDetail jobDetail = null;
            
            jobDetail = JobBuilder
                    .newJob(GoodsListing.class)
                    .withIdentity(name, group)
                    .usingJobData("task_id", task.getId())
                    .build();
            
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(name, group)
                    .startAt(task.getStartTime())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            task.setStatus("等待执行");
            taskRepository.save(task);
            return new PageInfo("success", "创建任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new PageInfo("error", "创建任务失败");
        }
    }
    
    /**
     * 下架任务
     * @param task
     * @return
     */
    public PageInfo goodDelistingTask(Task task){
        List<Task> tasks = getTasksByUser(task.getUser());
        for (Task item : tasks) {
            if (item.getStatus().contains("正在执行") || item.getStatus().contains("等待执行")) {
                return new PageInfo("error", "创建任务失败，完整上下架任务时不得有其他正在执行或等待执行的任务");
            }
        }
        if(flowUsage(task.getUser()) == false){
        	return new PageInfo("error","流量不足，请充值后再开启任务");
        }
        User user = task.getUser();
        try {
            taskRepository.save(task);
            String name = task.getId();
            String group = user.getNick();
            JobDetail jobDetail = null;
            
            jobDetail = JobBuilder
                    .newJob(GoodsDelisting.class)
                    .withIdentity(name, group)
                    .usingJobData("task_id", task.getId())
                    .build();
            
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(name, group)
                    .startAt(task.getStartTime())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            task.setStatus("等待执行");
            taskRepository.save(task);
            return new PageInfo("success", "创建任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new PageInfo("error", "创建任务失败");
        }
    }
    
    /**
	 * 流量使用情况
	 * @return true:可用  false：不可用
	 */
	public boolean flowUsage(User user){
		String usableFlowObject = stringRedisTemplate.opsForValue().get("usableFlow" + user.getNick());
		long usableFlow = usableFlowObject == null ? 0 : Long.parseLong(usableFlowObject);
		if(usableFlow > 0){
			return true;
		}
		return false;
		
	}
    
    public PageInfo addTask(Task task) {
        if (task.getEndTime() != null) {
            if (task.getStartTime().after(task.getEndTime())) {
                return new PageInfo("error", "创建任务失败，任务开始时间不能晚于结束时间");
            }
            if (task.getEndTime().after(task.getUser().getEndTime())) {
                return new PageInfo("error", "创建任务失败，任务结束时间不能晚于账户到期时间，请及时充值");
            }
            if(flowUsage(task.getUser()) == false){
            	return new PageInfo("error","流量不足，请充值后再开启任务");
            }
            List<Task> tasks = getTasksByUser(task.getUser());
            for (Task item : tasks) {
                Date startTime1 = item.getStartTime(); 
                Date endTime1 = item.getEndTime();
                if(startTime1 == null || endTime1 == null){
                	continue;
                }
                /*if (task.getDescription().contains("完整上下架")) {
                    return new PageInfo("error", "创建任务失败，有完整上下架任务还未完成");
                }*/
                if(item.getStatus() == null){
                	item.setStatus("");
                }
                if (startTime1.before(task.getEndTime()) && endTime1.after(task.getStartTime()) && !item.getStatus().contains("任务结束")) {
                    return new PageInfo("error", "创建任务失败，不得与已有任务时间段重叠");
                }
            }
        } else {
            List<Task> tasks = getTasksByUser(task.getUser());
            for (Task item : tasks) {
                if (item.getStatus().contains("正在执行") || item.getStatus().contains("等待执行")) {
                    return new PageInfo("error", "创建任务失败，完整上下架任务时不得有其他正在执行或等待执行的任务");
                }
            }
        }
        User user = task.getUser();
        try {
            taskRepository.save(task);
            String name = task.getId();
            String group = user.getNick();
            JobDetail jobDetail;
            if (task.getEndTime() != null) {
                jobDetail = JobBuilder
                        .newJob(Job1.class)
                        .withIdentity(name, group)
                        .usingJobData("task_id", task.getId())
                        .build();
            } else {
                jobDetail = JobBuilder
                        .newJob(Job2.class)
                        .withIdentity(name, group)
                        .usingJobData("task_id", task.getId())
                        .build();
            }
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(name, group)
                    .startAt(task.getStartTime())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            task.setStatus("等待执行");
            taskRepository.save(task);
            return new PageInfo("success", "创建任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new PageInfo("error", "创建任务失败");
        }
    }

    public PageInfo removeTaskById(User user, String id) {
        Task task = taskRepository.findTaskById(id);
        if (task == null) {
            return new PageInfo("error", "删除任务失败，非法操作");
        }
        String taskId = task.getId();
        String taskFlag = (String) redis.get("task" + taskId);
        if(taskFlag != null && "true".equals(taskFlag)){
            return new PageInfo("error", "任务还在进行中，请先中止");
        }
        if(!task.getUser().getNick().equals(user.getNick())) {
            return new PageInfo("error", "删除任务失败，非法操作");
        } else if (task.getStatus().contains("正在执行")) {
            return new PageInfo("error", "删除任务失败，任务正在进行请先中止");
        }
        try {
            taskRepository.delete(task);
            scheduler.deleteJob(JobKey.jobKey(task.getUser().getNick(), id));
            return new PageInfo("success", "删除任务成功");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new PageInfo("error", "删除任务失败");
        }
    }

    public Page<Task> getTasksByUser(User user, int no, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(no, size, sort);
        return taskRepository.findTasksByUser(user, pageable);
    }

    private List<Task> getTasksByUser(User user) {
        return taskRepository.findTasksByUser(user);
    }

    @CacheEvict(value = "task")
    public String stopTaskById(String id, String number) {
    	//String taskStatus = (String) redis.get("task" + id);
    	String taskStatus = stringRedisTemplate.opsForValue().get("task" + id);
    	if(taskStatus == null || "".equals(taskStatus) || taskStatus.equals("null")){
            Task task = this.getTaskById(id);
        	task.setStatus("任务结束");
    		this.saveTask(task);
    		return "任务已中止";
    	}
    	/**
    	 * 标记任务中止，告知其他线程
    	 */
    	//redis.remove("task" + id);
    	stringRedisTemplate.delete("task" + id);
        Task task = taskRepository.findTaskById(id);
        if(task.getStatus().contains("正在中止")){
        	return "任务正在中止商品，请勿重复操作";
        }
        
        task.setStatus("正在中止任务");
        this.saveTask(task);
        if(task.getStatus().contains("正在恢复")){
        	return "任务正在恢复商品，请勿重复操作";
        }
        if (task.getStatus().equals("等待执行")) {
            return "任务还未开始，不能中止";
        } else if (task.getStatus().contains("任务结束")) {
            return "任务已结束，不能中止";
        } else {
            try {
            	/*MainTask work = Job1.jobs.get(task.getId());
            	if(work == null){
            		task.setStatus("任务结束");
            		logger.info("任务不存在,,,结束");
            		//task.setStatus("任务结束(成功处理" + task.getNum() + "次)");
            		this.saveTask(task);
            	}else{
                    work.breakDown();     //中止任务
            	}*/
                return "任务中止成功，正在恢复商品初始状态请等待";
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return "任务中止失败";
            }
        }
    }

    @Cacheable(value = "task")
    public Task findTaskById(String task_id) {
        return taskRepository.findTaskById(task_id);
    }

    @CachePut(value = "task")
    public Task getTaskById(String task_id) {
        return taskRepository.findTaskById(task_id);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    //获取所有正在执行任务
    public Page<Task> getAllTasksByStatus(String s, int no, int size) {
        Pageable pageable = PageRequest.of(no, size);
        return taskRepository.findTasksByStatusContains(s, pageable);
    }
}
