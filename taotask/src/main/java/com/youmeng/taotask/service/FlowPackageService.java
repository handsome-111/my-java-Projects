package com.youmeng.taotask.service;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youmeng.common.base.taotask.entity.FlowPackage;

/**
 * <p>
 * 流量包 服务类
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-27
 */
public interface FlowPackageService extends IService<FlowPackage> {
	/**
	 * 生成流量包
	 * @param flowPackage
	 * @return	1：成功	0：失败
	 */
	int insertFlowPackage(FlowPackage flowPackage);
	/**
	 * 获取流量包
	 * @param page	分页
	 * @param owner	拥有者
	 * @param user	使用者
	 * @param order	排序
	 * @param field	字段
	 * @return	流量包的分页信息
	 */
	IPage<FlowPackage> getFlowPackage(Page page,String owner,String user,String order,String field);
	/**
	 * 使用流量包
	 * @return
	 */
	String useingFlowPackage(@Param("flowId")String flowId,@Param("user")String user,@Param("usageTime")Date usageTime);
	/**
	 * 根据密钥获取流量包
	 * @param flowId	密钥
	 * @param owner		拥有者
	 * @return
	 */
	String getFlowPackageByKey(@Param("flowId")String flowId,@Param("owner")String owner);
}
