package com.lyd.springcloud;

import com.lyd.myrule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * @program: springcloud
 * @description: 80主启动类
 * @author: CodeLiu
 * @create: 2020-07-30 20:04
 **/
@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "ClOUD-PAYMENT-SERVICE",configuration = MySelfRule.class)
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class,args);
    }
}
