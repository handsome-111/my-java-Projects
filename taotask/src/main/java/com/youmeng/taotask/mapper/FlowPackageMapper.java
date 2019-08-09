package com.youmeng.taotask.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youmeng.common.base.taotask.entity.FlowPackage;

/**
 * <p>
 * 流量包 Mapper 接口
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-27
 */
@Mapper
public interface FlowPackageMapper extends BaseMapper<FlowPackage> {
	
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
	IPage<FlowPackage> getFlowPackage(Page page,@Param("owner") String owner,@Param("user") String user,@Param("order") String order,@Param("field")String field);
	/**
	 * 使用流量包
	 * @param flowPackage	流量包
	 * @return
	 */
	int useingFlowPackage(@Param("flowId")String flowId,@Param("user")String user,@Param("usageTime")Date usageTime);
}




