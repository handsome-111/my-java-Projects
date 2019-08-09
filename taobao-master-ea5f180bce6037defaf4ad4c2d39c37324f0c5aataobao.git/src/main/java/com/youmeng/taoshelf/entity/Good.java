package com.youmeng.taoshelf.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.util.Comparator;
import java.util.Date;

public class Good implements Comparator<Good>{

    @JSONField(name = "num_iid")
    private Long numIid;

    private String title;

    @Transient
    @JSONField(name = "list_time")
    private Date listTime;

    @Transient
    @JSONField(name = "delist_time")
    private Date delistTime;

    @JSONField(name = "approve_status")
    private String approveStatus;

    @Transient
    private Date modified;

    @Transient
    private Long num;
    
    @Transient
    private int cid;	//商品所属的店铺内卖家自定义类目列表 
    
    @Transient
    private int sold_quantity;		//商品销量
    
    @Transient
    private String seller_cids;		//商品所属的店铺内卖家自定义类目列表
    public Good() {
    }

    @Override
    public int compare(Good good1, Good good2) {
    	if(good1.sold_quantity > good2.sold_quantity){
    		return 1;
    	}
    	if(good1.sold_quantity < good2.sold_quantity){
    		return -1;
    	}
    	return 0;
    }
    
    public Good(Long numIid, String title, String approveStatus) {
        this.numIid = numIid;
        this.title = title;
        this.approveStatus = approveStatus;
    }

    public String getSeller_cids() {
		return seller_cids;
	}

	public void setSeller_cids(String seller_cids) {
		this.seller_cids = seller_cids;
	}

	public int getSold_quantity() {
		return sold_quantity;
	}

	public void setSold_quantity(int sold_quantity) {
		this.sold_quantity = sold_quantity;
	}

	public Long getNumIid() {
        return numIid;
    }

    public void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getListTime() {
        return listTime;
    }

    public void setListTime(Date listTime) {
        this.listTime = listTime;
    }

    public Date getDelistTime() {
        return delistTime;
    }

    public void setDelistTime(Date delistTime) {
        this.delistTime = delistTime;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	@Override
    public String toString() {
        return "Good{" +
                "numIid=" + numIid +
                ", title='" + title + '\'' +
                ", listTime=" + listTime +
                ", delistTime=" + delistTime +
                ", approveStatus='" + approveStatus + '\'' +
                ", modified=" + modified +
                ", num=" + num +
                ", cid=" + cid +
                '}';
    }
}
