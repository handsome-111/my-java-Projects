package com.youmeng.taoshelf.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-06
 */
@TableName("taskLog")
public class Tasklog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务日志id
     */
    @TableId(value = "tasklogId", type = IdType.AUTO)
    private Integer tasklogId;

    /**
     * 消息
     */
    private String msg;

    /**
     * 日志日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 所属任务
     */
    private Task task;

    /**
     * 商品的numIid
     */
    private String goodid;
    /**
     * 商品标题
     */
    private String title;
    
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTasklogId() {
        return tasklogId;
    }

    public void setTasklogId(Integer tasklogId) {
        this.tasklogId = tasklogId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTime() throws Exception {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.parse((sdf.format(time)));
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	

	public String getGoodid() {
		return goodid;
	}

	public void setGoodid(String goodid) {
		this.goodid = goodid;
	}

	@Override
    public String toString() {
        return "Tasklog{" +
        "tasklogId=" + tasklogId +
        ", msg=" + msg +
        ", time=" + time +
        ", taskId=" + task +
        ", goodid=" + goodid +
        "}";
    }
}
