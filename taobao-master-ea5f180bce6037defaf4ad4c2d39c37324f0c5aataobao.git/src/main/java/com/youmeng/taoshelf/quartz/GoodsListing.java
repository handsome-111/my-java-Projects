package com.youmeng.taoshelf.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class GoodsListing extends QuartzJobBean{
	@Autowired
    private RestTemplate restTemplate;
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        //从context中获取task_id、user
        String taskId = jobDataMap.getString("task_id");
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
		paramMap.add("taskId", taskId);
	    String response = restTemplate.postForObject("http://localhost:81/taotask/startGoodsListingTask", paramMap, String.class);
	}
}
