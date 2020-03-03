package com.whj.study.solr.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;
/**
 * 商品，存储于Mongodb
 * @author Administrator
 *
 */
@SolrDocument(collection = "product")
public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	//商品id
	private String id;
	
	//商品描述
	private String itemDesc;
	
	//商品名称
	private String itemName;
	
	//最低价格
	private String lowPrice;
	
	//最高价格
	private String highPrice;
	
	//分销分成比例
	private String fxFeeRate;
	
	//卖家Id,目前没用的字段
	private Number sellerId;
	
	//商品编号
	private String merchantCode;
	
	//商品销量
	private String sold;
	
	//库存量
	private Number stock;
	
	//是否包邮：0不包邮，1包邮
	private Number freeDelivery;
	
	//商品价格,在Mongodb中可以排序
	private String price;	
	
	//商品图片描述
	private List<String> titles;
	
	//图片略缩图，缩小后的
	private List<String> thumbImgs;
	
	//商品图片(展示商品的图片/视频)
	private List<String> imgs;

	//商品描述
	private String itemComment;
	

	//头图
	private String imgHead;
	
	//可以购买的库存数
	private Number buyStock;
	
	
	public Product(){}
	
	public Product(String id) {
		super();
		this.id = id;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getFxFeeRate() {
		return fxFeeRate;
	}

	public void setFxFeeRate(String fxFeeRate) {
		this.fxFeeRate = fxFeeRate;
	}



	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}



	public Number getStock() {
		return stock;
	}

	public void setStock(Number stock) {
		this.stock = stock;
	}


	public Number getFreeDelivery() {
		return freeDelivery;
	}

	public void setFreeDelivery(Number freeDelivery) {
		this.freeDelivery = freeDelivery;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public List<String> getThumbImgs() {
		return thumbImgs;
	}

	public void setThumbImgs(List<String> thumbImgs) {
		this.thumbImgs = thumbImgs;
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}

	public String getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}

	public String getSold() {
		return sold;
	}

	public void setSold(String sold) {
		this.sold = sold;
	}

	public String getItemComment() {
		return itemComment;
	}

	public void setItemComment(String itemComment) {
		this.itemComment = itemComment;
	}


	public String getImgHead() {
		return imgHead;
	}

	public void setImgHead(String imgHead) {
		this.imgHead = imgHead;
	}

	public Number getSellerId() {
		return sellerId;
	}

	public void setSellerId(Number sellerId) {
		this.sellerId = sellerId;
	}


	public Number getBuyStock() {
		return buyStock;
	}

	public void setBuyStock(Number buyStock) {
		this.buyStock = buyStock;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", itemDesc=" + itemDesc + ", itemName=" + itemName + ", lowPrice=" + lowPrice
				+ ", highPrice=" + highPrice + ", fxFeeRate=" + fxFeeRate + ", sellerId=" + sellerId + ", merchantCode="
				+ merchantCode + ", sold=" + sold + ", stock=" + stock + ", freeDelivery=" + freeDelivery + ", price="
				+ price + ", titles=" + titles + ", thumbImgs=" + thumbImgs + ", imgs=" + imgs + ", itemComment="
				+ itemComment + ", imgHead=" + imgHead + ", buyStock=" + buyStock + "]";
	}

	

	
	

	



}








