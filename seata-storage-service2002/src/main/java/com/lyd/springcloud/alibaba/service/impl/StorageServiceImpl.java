package com.lyd.springcloud.alibaba.service.impl;

import com.lyd.springcloud.alibaba.dao.StorageDao;
import com.lyd.springcloud.alibaba.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:19
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Service
public class StorageServiceImpl implements StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);
    @Resource
    private StorageDao storageDao;
    /**
     * @Description: 扣减库存
     * @Param: [productId, count]
     * @return: void
     * @Author: Liuyunda
     * @Date: 2020/12/22
     */
    @Override
    public void decrease(Long productId, Integer count) {
        LOGGER.info("------->storage-service中扣减库存开始");
        storageDao.decrease(productId,count);
        LOGGER.info("------->storage-service中扣减库存结束");
    }
}
