package com.youmeng.taoshelf.serviceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youmeng.common.base.taotask.entity.FlowPackage;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.mapper.FlowPackageMapper;
import com.youmeng.taoshelf.service.FlowPackageService;
import com.youmeng.taoshelf.service.UserService;

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
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private UserService userService;
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
	public String useingFlowPackage(String flowId, String nick, Date usageTime) {
		User user = userService.getUserByNick(nick);
		FlowPackage flowPackage = this.getById(flowId);
		if(flowPackage != null && flowPackage.getUsageTime() == null){
			int result = flowPackageMapper.useingFlowPackage(flowId, user.getNick(), new Date());
			switch(result){
				case 1 : {
					/**
					 * 流量充值
					 */
					long usableFlow = flowUsage(user.getNick());
					usableFlow = flowPackage.getFlowMax() + usableFlow;
					redisTemplate.opsForValue().set("usableFlow" + user.getNick(), usableFlow+"");
					return "1";			//流量包充值成功
				}
				default :{
					return "0";			//流量包充值失败
				}
			}
		}else{
			return "流量包失效";
		}
	}
	/**
	 * 流量使用情况
	 */
	@Override
	public long flowUsage(String nick) {
		String usableFlowObject = redisTemplate.opsForValue().get("usableFlow" + nick);
		long usableFlow = usableFlowObject == null ? 0 : Long.parseLong(usableFlowObject);
		return usableFlow;
	}

	/**
	 * 根据密钥获取流量包
	 */
	@Override
	public int getFlowPackageByKey(String flowId, String owner) {
		int result = flowPackageMapper.getFlowPackageByKey(flowId, owner,new Date());
		switch(result){
		case 1 : {
			return 1;		//流量包获取成功;
		}
		default :{
			return 0;		//"无法获取流量包(可能原因：1.流量包已被使用2.密钥不正确)";
		}
	}
	}
}
