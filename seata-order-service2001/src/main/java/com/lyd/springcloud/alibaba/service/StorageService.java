package com.lyd.springcloud.alibaba.service;

import com.lyd.springcloud.alibaba.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Liuyunda
 * @Date 2020/12/21 23:47
 * @Email man021436@163.com
 * @Description: DOTO
 */
@FeignClient(value = "seata-storage-service")
public interface StorageService {
    /**
     * @Description: 扣减库存
     * @Param: [productId, count]
     * @return: com.lyd.springcloud.alibaba.domain.CommonResult
     * @Author: Liuyunda
     * @Date: 2020/12/21
     */
    @PostMapping(value = "/storage/decrease")
    CommonResult decrease(@RequestParam("productId")Long productId,@RequestParam("count")Integer count);
}
