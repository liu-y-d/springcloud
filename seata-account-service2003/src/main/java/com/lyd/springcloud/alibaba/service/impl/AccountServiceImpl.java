package com.lyd.springcloud.alibaba.service.impl;

import com.lyd.springcloud.alibaba.dao.AccountDao;
import com.lyd.springcloud.alibaba.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:29
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Resource
    AccountDao accountDao;
    /**
     * @Description: 扣减账户余额
     * @Param: [userId, money]
     * @return: void
     * @Author: Liuyunda
     * @Date: 2020/12/22
     */
    @Override
    public void decrease(Long userId, BigDecimal money) {
        LOGGER.info("------->account-service中扣减账户余额开始");
        //模拟超时异常，全局事务回滚
        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
        accountDao.decrease(userId,money);
        LOGGER.info("------->account-service中扣减账户余额结束");
    }
}
