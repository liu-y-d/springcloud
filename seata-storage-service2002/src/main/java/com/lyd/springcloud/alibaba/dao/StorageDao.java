package com.lyd.springcloud.alibaba.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:16
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Mapper
public interface StorageDao {
    /**
     * @Description: 扣减库存
     * @Param: [productId, count]
     * @return: void
     * @Author: Liuyunda
     * @Date: 2020/12/22
     */
    void decrease(@Param("productId") Long productId, @Param("count") Integer count);
}
