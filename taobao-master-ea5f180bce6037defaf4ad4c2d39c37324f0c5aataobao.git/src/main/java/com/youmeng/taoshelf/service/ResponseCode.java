package com.youmeng.taoshelf.service;
/**
 * 响应代码
 * @author Administrator
 *
 */
public enum ResponseCode {
	/**
	 * 正常响应代码
	 */
	ITEM_UPDATE_LISTING_RESPONSE("item_update_listing_response","正常响应"),
	/**
	 * 异常响应代码
	 */
	ERROR_CODE_50_IC_CHECKSTEP_NO_PERMISSION("isv.item-listing-service-error:IC_CHECKSTEP_NO_PERMISSION","未通过认证或已被处罚"),
	ERROR_CODE_50_QUANTITY_ITEM_CAT_TOO_LARGE("isv.item-listing-service-error:QUANTITY_ITEM_CAT_TOO_LARGE","宝贝总数已超过此类目宝贝数量限额"),	
	ERROR_CODE_15_TIMEOUT("isp.top-remote-connection-timeout","远程服务调用超时"),
	ERROR_CODE_530_BUSY("isp.system-busy","调用接口频率太快"),
	ERROR_CODE_7_LIMITED_BY_API_ACCESS_COUNT("accesscontrol.limited-by-api-access-count","访问控制,受api访问计数限制"),
	ERROR_CODE("other","其他的错误信息"),
	ERROR_CODE_ERROR_CATEGORY_MARGIN_ISNOT_ENOUGH("isv.error-category-margin-isnot-enough","错误信息该类目下发布全新商品，您需要提交保证金不少于30000.0元");
	private String sub_msg;		//错误信息
	private String sub_code;	//错误代码
	private ResponseCode(String sub_code,String sub_msg){
		this.sub_code = sub_code;
		this.sub_msg = sub_msg;
	}
	public String getSub_msg(){
		return sub_msg;
	}
	public String getSub_code(){
		return sub_code;
	}
	public String toString(){
		return this.getSub_code();
	}
	/**
	 * 判断是否存在指定错误代码
	 * @param code
	 * @return
	 */
	public static boolean contains(String code){
		for(ResponseCode errorCode : ResponseCode.values()){
			if(errorCode.sub_code.equalsIgnoreCase(code)){
				return true;
			}
		}
		return false;
	}
	
	public boolean equals(String sub_code){
		if(this.sub_code.equalsIgnoreCase(sub_code)){
			return true;
		}
		return false;
	}
	
	public boolean equals(ResponseCode responseCode){
		if(this.sub_code.equalsIgnoreCase(responseCode.getSub_code())){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(ResponseCode.contains("isv.item-listing-service-error:IC_CHECKSTEP_NO_PERMISSION"));
	}
}
