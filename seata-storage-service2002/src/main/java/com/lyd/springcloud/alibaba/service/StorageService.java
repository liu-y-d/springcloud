package com.lyd.springcloud.alibaba.service;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:18
 * @Email man021436@163.com
 * @Description: DOTO
 */
public interface StorageService {
    /**
     * @Description: 扣减库存
     * @Param: [productId, count]
     * @return: void
     * @Author: Liuyunda
     * @Date: 2020/12/22
     */
    void decrease(Long productId, Integer count);
}
