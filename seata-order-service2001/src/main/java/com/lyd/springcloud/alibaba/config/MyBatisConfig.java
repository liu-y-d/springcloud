package com.lyd.springcloud.alibaba.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:04
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Configuration
@MapperScan({"com.lyd.springcloud.alibaba.dao"})
public class MyBatisConfig {
}
