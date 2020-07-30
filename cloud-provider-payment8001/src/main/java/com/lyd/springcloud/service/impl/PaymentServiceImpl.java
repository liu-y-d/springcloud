package com.lyd.springcloud.service.impl;

import com.lyd.springcloud.dao.PaymentDao;
import com.lyd.springcloud.entities.Payment;
import com.lyd.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: springcloud
 * @description: 接口实现类
 * @author: CodeLiu
 * @create: 2020-07-29 22:04
 **/
@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }
}
