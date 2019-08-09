package com.youmeng.security;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configurable
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/css/**","/index","/a/**").permitAll()		//不需要验证
			.antMatchers("/b/**").hasRole("USER")						//需要用户权限
			.and()
			.exceptionHandling().accessDeniedPage("/401");
		http.logout().logoutSuccessUrl("/");
	}
	/**
	 * 1.应用的每一个请求都需要认证
	 * 2.自动生成登录表单
	 * 3.可以使用username和password来进行认证
	 * 4.用户可以注销
	 * 5.阻止了CSRF攻击
	 * 6.Session Fixation保护
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("root")
			.password("root")
			.roles("USER");
	}
}
