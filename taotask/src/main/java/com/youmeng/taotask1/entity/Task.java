/*package com.youmeng.taotask1.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class Task implements Serializable {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    @CreatedDate
    private Date createTime;

    private Date startTime;

    private Date endTime;

    private String status;

    @Transient
    private String description;

    //@ManyToOne
    private User user;

    private Long num;
    
    private int taskType = 1;		//任务类型：1:循环上下架/2:完整上下架 3.上架4.下架等
    
    private List<Tasklog> taskLogs;
    
    public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
    	long endTimeStamp = 0;
    	long startTimeStamp = startTime.getTime();
    	String minute = "";
    	if(endTime != null){
    		endTimeStamp = endTime.getTime();
            minute = (startTimeStamp - endTimeStamp)/60000 + "分钟";
    	}
        
    	switch(taskType){
    		case 1:{
    	        return type + minute + "上下架";
    		}
    		case 2:{
    			return type + minute + "完整上下架";
    		}
    		case 3:{
            	return type + minute +"完整上架";
    		}
    		case 4:{
            	return type + minute +"完整下架";
    		}
    		*//**
    		 * 默认为1
    		 *//*
    		default:{
    	        return type + minute + "上下架";
    		}
    	}
    }

    public List<Tasklog> getTaskLogs() {
		return taskLogs;
	}

	public void setTaskLogs(List<Tasklog> taskLogs) {
		this.taskLogs = taskLogs;
	}

	public void setDescription(String description) {
        this.description = description;
    }
}
*/