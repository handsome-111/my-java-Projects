package com.forezp.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forezp.domain.User;
import com.forezp.mapper.UserMapper;

/**
 * Created by fangzhipeng on 2017/5/10.
 */
@Service
public class UserServiceDetail implements UserDetailsService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		QueryWrapper query = new QueryWrapper();
		query.eq("username", username);
		User user = userMapper.selectOne(query);
		
		System.out.println("user:" + user);
		return user;
	}

}
