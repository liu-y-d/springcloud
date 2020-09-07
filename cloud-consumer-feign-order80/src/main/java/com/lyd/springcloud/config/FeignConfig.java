package com.lyd.springcloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @Author Liuyunda
 * @Date 2020/8/26 20:29
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level FeignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
