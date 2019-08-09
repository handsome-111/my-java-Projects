package com.youmeng.taoshelf.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @ManyToOne
    private User user;

    private Long num;
    
    private int taskType = 1;		//任务类型：1:循环上下架/2:完整上下架 3.上架4.下架等
    
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
    		/**
    		 * 默认为1
    		 */
    		default:{
    	        return type + minute + "上下架";
    		}
    	}
    }

    public void setDescription(String description) {
        this.description = description;
    }

	@Override
	public String toString() {
		return "Task [id=" + id + ", type=" + type + ", createTime=" + createTime + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", status=" + status + ", description=" + description + ", user=" + user
				+ ", num=" + num + ", taskType=" + taskType + "]";
	}
    
}
