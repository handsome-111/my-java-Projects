package com.youmeng.taotask.serviceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youmeng.common.base.taotask.entity.FlowPackage;
import com.youmeng.taotask.mapper.FlowPackageMapper;
import com.youmeng.taotask.service.FlowPackageService;

/**
 * <p>
 * 流量包 服务实现类
 * </p>
 *
 * @author Mr.hj
 * @since 2019-01-27
 */
@Service
public class FlowPackageServiceImpl extends ServiceImpl<FlowPackageMapper, FlowPackage> implements FlowPackageService {
	@Autowired
	private FlowPackageMapper flowPackageMapper;
	@Override
	public int insertFlowPackage(FlowPackage flowPackage) {
		return flowPackageMapper.insert(flowPackage);
	}
	@Override
	public IPage<FlowPackage> getFlowPackage(Page page, String owner, String user, String order, String field) {
		return flowPackageMapper.getFlowPackage(page, owner, user, order, field);
	}
	/**
	 * 使用流量包
	 */
	@Override
	public String useingFlowPackage(String flowId, String user, Date usageTime) {
		FlowPackage flowPackage = this.getById(user);
		System.out.println(flowPackage + "---" + flowPackage.getUsageTime());
		if(flowPackage != null && flowPackage.getUsageTime() == null){
			int result = flowPackageMapper.useingFlowPackage(flowId, user, new Date());
			switch(result){
				case 1 : {
					return "流量包已生效";
				}
				default :{
					return "使用流量包异常";
				}
			}
		}else{
			return "流量包失效";
		}
	}
	@Override
	public String getFlowPackageByKey(String flowId, String owner) {
		// TODO Auto-generated method stub
		return null;
	}
}
