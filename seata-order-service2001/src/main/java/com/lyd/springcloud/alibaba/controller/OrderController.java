package com.lyd.springcloud.alibaba.controller;

import com.lyd.springcloud.alibaba.domain.CommonResult;
import com.lyd.springcloud.alibaba.domain.Order;
import com.lyd.springcloud.alibaba.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:02
 * @Email man021436@163.com
 * @Description: DOTO
 */
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;

    @GetMapping("/order/create")
    public CommonResult create(Order order){
        orderService.create(order);
        return new CommonResult(200,"订单创建成功！");
    }
}
