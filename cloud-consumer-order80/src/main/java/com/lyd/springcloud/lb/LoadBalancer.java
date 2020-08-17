package com.lyd.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @Author Liuyunda
 * @Date 2020/8/16 20:50
 * @Email man021436@163.com
 * @Description: DOTO
 */
public interface LoadBalancer {
    ServiceInstance instances(List<ServiceInstance> serviceInstances);
}
