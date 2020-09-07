package com.lyd.springcloud.service;

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

    public String paymentInfo_TimeOut(Integer id){
        int timeNumber = 3;
        // 暂停三秒线程
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池："+Thread.currentThread().getName()+" paymentInfo_TimeOut,id"+id+"\t"+"呜呜 "+"耗时(秒)"+timeNumber;
    }
}
