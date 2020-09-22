package com.lyd.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author Liuyunda
 * @Date 2020/9/7 20:22
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Service
public class PaymentService {
    /**
     * @Description: 正常访问肯定通过
     * @Param: [id]
     * @return: java.lang.String
     * @Author: Liuyunda
     * @Date: 2020/9/7
     */
    public String paymentInfo_OK(Integer id){
        return "线程池："+Thread.currentThread().getName()+" paymentInfo_OK,id"+id+"\t"+"哈哈";
    }

    // 如果paymentInfo_TimeOut出事了那么paymentInfo_TimeOutHandler为我兜底
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public String paymentInfo_TimeOut(Integer id){
        // 暂停三秒线程
        try {
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // int age = 10/0;
        return "线程池："+Thread.currentThread().getName()+" paymentInfo_TimeOut,id"+id+"\t"+"哈哈 ";
    }
    public String paymentInfo_TimeOutHandler(Integer id){
        return "线程池："+Thread.currentThread().getName()+" 系统繁忙请稍后再试,id"+id+"\t"+"呜呜";
    }
}
