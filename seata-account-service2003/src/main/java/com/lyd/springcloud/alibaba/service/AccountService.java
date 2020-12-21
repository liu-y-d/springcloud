package com.lyd.springcloud.alibaba.service;

import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:28
 * @Email man021436@163.com
 * @Description: DOTO
 */
public interface AccountService {
    /**
     * @Description: 扣减账户余额
     * @Param: [userId, money]
     * @return: void
     * @Author: Liuyunda
     * @Date: 2020/12/22
     */
    void decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
