package com.lyd.springcloud.controller;

import com.lyd.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liuyunda
 * @Date 2020/9/22 19:37
 * @Email man021436@163.com
 * @Description: DOTO
 */
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "paymentGlobalFallbackMethod")
public class OrderHystrixController {
    @Resource
    private PaymentHystrixService paymentHystrixService;
    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result = paymentHystrixService.paymentInfo_OK(id);
        return result;
    }
    @GetMapping("/consumer/payment/hystrix/timeou/{id}")
    // 如果paymentInfo_TimeOut出事了那么paymentInfo_TimeOutHandler为我兜底
    // @HystrixCommand(fallbackMethod = "paymentTimeOutFallBackHandler", commandProperties = {
    //         @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")
    // })
    @HystrixCommand
    public String paymentInfo_TimeOut(@PathVariable("id")Integer id){
        int age = 10/0;
        String result = paymentHystrixService.paymentInfo_TimeOut(id);
        return result;
    }
    public String paymentTimeOutFallBackHandler(@PathVariable("id")Integer id){
        return "线程池："+Thread.currentThread().getName()+" 系统繁忙请稍后再试,id"+id+"\t"+"呜呜我是消费者80";
    }

    // 下面是全局fallback
    public String paymentGlobalFallbackMethod(){
        return "全局异常，请稍后再试！";
    }

}
