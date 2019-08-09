package com.youmeng.taoshelf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 调用淘宝接口响应代码，主要获取淘宝最新的错误信息来及时更新应用
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-07
 */
@TableName("response_message")
public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 代码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 副代码
     */
    private String subCode;

    /**
     * 副信息
     */
    private String subMsg;

    /**
     * 请求id
     */
    private String requestId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
        "id=" + id +
        ", code=" + code +
        ", msg=" + msg +
        ", subCode=" + subCode +
        ", subMsg=" + subMsg +
        ", requestId=" + requestId +
        "}";
    }
}
