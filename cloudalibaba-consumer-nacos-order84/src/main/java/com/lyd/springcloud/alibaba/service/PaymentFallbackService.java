package com.lyd.springcloud.alibaba.service;

import com.lyd.springcloud.entities.CommonResult;
import com.lyd.springcloud.entities.Payment;
import org.springframework.stereotype.Component;

/**
 * @Author Liuyunda
 * @Date 2020/12/16 23:43
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Component
public class PaymentFallbackService implements PaymentService{
    @Override
    public CommonResult<Payment> paymentSQL(Long id)
    {
        return new CommonResult<>(44444,"服务降级返回,---PaymentFallbackService",new Payment(id,"errorSerial"));
    }
}
