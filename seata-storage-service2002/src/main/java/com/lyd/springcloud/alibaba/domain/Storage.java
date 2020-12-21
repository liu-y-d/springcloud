package com.lyd.springcloud.alibaba.domain;

import lombok.Data;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:14
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Data
public class Storage {
    private Long id;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 总库存
     */
    private Integer total;

    /**
     * 已用库存
     */
    private Integer used;

    /**
     * 剩余库存
     */
    private Integer residue;
}
