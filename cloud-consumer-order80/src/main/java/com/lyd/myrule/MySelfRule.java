package com.lyd.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Liuyunda
 * @Date 2020/8/7 21:37
 * @Email man021436@163.com
 * @Description: Ribbon规则类
 */
@Configuration
public class MySelfRule {
    @Bean
    public IRule myRule(){
        // 定义为随机
        return new RandomRule();
    }
}
