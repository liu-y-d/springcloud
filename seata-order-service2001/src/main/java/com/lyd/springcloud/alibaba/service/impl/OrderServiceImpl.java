package com.lyd.springcloud.alibaba.service.impl;

import com.lyd.springcloud.alibaba.dao.OrderDao;
import com.lyd.springcloud.alibaba.domain.Order;
import com.lyd.springcloud.alibaba.service.AccountService;
import com.lyd.springcloud.alibaba.service.OrderService;
import com.lyd.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Liuyunda
 * @Date 2020/12/21 23:48
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private StorageService storageService;
    @Resource
    private AccountService accountService;



    @Override
    @GlobalTransactional(name = "lyd-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        // 1.新建订单
        log.info("----->开始新建订单");
        orderDao.create(order);
        // 2.减库存
        log.info("----->订单微服务开始调用库存，做扣减count");
        storageService.decrease(order.getProductId(),order.getCount());
        log.info("----->订单微服务开始调用库存，做扣减end");

        // 3.减余额
        log.info("----->订单微服务开始调用账户，做扣减money");
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info("----->订单微服务开始调用账户，做扣减end");

        // 4.修改订单的状态，从0->1标识已经完成
        log.info("----->修改订单状态开始");
        orderDao.update(order.getUserId(),0);
        log.info("----->修改订单状态结束");

        log.info("----->订单以完成！！！");
    }
}
