package com.lyd.springcloud.service;

import org.springframework.stereotype.Component;

/**
 * @Author Liuyunda
 * @Date 2020/9/22 20:44
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Component
public class PaymentFallBackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "----PaymentFallBackService,fallback-paymentInfo_OK";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "----PaymentFallBackService,fallback-paymentInfo_TimeOut";
    }
}
