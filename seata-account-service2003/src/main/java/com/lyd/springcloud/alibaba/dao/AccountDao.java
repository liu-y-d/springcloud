package com.lyd.springcloud.alibaba.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:27
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Mapper
public interface AccountDao {
    /**
     * @Description: 扣减账户余额
     * @Param: [userId, money]
     * @return: void
     * @Author: Liuyunda
     * @Date: 2020/12/22
     */
    void decrease(@Param("userId") Long userId, @Param("money") BigDecimal money);
}
