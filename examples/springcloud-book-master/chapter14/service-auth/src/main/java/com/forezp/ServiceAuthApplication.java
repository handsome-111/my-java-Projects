package com.forezp;

import com.forezp.service.security.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@SpringBootApplication
@EnableResourceServer	
public class ServiceAuthApplication {
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	public static void main(String[] args) {
		SpringApplication.run(ServiceAuthApplication.class, args);
	}


	@Configuration
	@EnableAuthorizationServer		//开启授权服务功能
	protected  class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

		//private TokenStore tokenStore = new InMemoryTokenStore();

		JdbcTokenStore tokenStore=new JdbcTokenStore(dataSource);

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Autowired
		private UserServiceDetail userServiceDetail;


		/**
		 * 配置客户端的基本信息
		 */
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

			clients.inMemory()
					.withClient("browser")		//创建名为browser的客户端
					.authorizedGrantTypes("refresh_token", "password")	//验证类型
					.scopes("ui")				//客户端的域为ui
					.and()
					.withClient("service-hi")	//创建另一个客户端
					.secret("123456")
					.authorizedGrantTypes("client_credentials", "refresh_token","password")
					.scopes("server");

		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints
					.tokenStore(tokenStore)		//token的存储方式
					.authenticationManager(authenticationManager)	//是在Security设置的认证管理器的bean
					.userDetailsService(userServiceDetail);
		}

		/**
		 * 获取token的策略,该例子对token请求不进行拦截
		 */
		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer
					.tokenKeyAccess("permitAll()")
					.checkTokenAccess("isAuthenticated()");

		}
	}
}
