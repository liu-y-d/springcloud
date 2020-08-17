package com.lyd.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @program: springcloud
 * @description: 配置类
 * @author: CodeLiu
 * @create: 2020-07-30 20:12
 **/
@Configuration
public class ApplicationContextConfig {

    @Bean
    // 使用@LoadBalanced注解赋予RestTemplate负责均衡能力
    // @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
