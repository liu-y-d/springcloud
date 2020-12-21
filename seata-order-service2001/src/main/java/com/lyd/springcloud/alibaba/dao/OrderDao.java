package com.lyd.springcloud.alibaba.dao;

import com.lyd.springcloud.alibaba.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Liuyunda
 * @Date 2020/12/21 23:31
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Mapper
public interface OrderDao {
    /**
     * @Description: 1.新建订单
     * @Param: [order]
     * @return: void
     * @Author: Liuyunda
     * @Date: 2020/12/21
     */
    void create(Order order);

    /**
     * @Description: 2.修改订单状态，从0->1
     * @Param: [userId, status]
     * @return: void
     * @Author: Liuyunda
     * @Date: 2020/12/21
     */
    void update(@Param("userId")Long userId,@Param("status")Integer status);
}
