package com.forezp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import feign.RequestInterceptor;

/**
 * 用于访问被OAuth2保护的资源
 * @author Administrator
 *
 */
@EnableOAuth2Client		//开启Oauth2功能
@EnableConfigurationProperties
@Configuration
public class OAuth2ClientConfig {


	/**
	 * 配置受保护资源的信息
	 * @return
	 */
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    /**
     * 配置过滤器，存储当前请求和上下文
     *  @EnableOAuth2Client.
     * 1.oauth2ClientContextFilter
     * 2.AccessTokenRequest
     */
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(){
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
    }

    /**
     * 在Request域内创建AccessTokenRequest类型的bean
     * @return
     */
    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }
}
