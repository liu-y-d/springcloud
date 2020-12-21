package com.lyd.springcloud.alibaba.controller;

import com.lyd.springcloud.alibaba.domain.CommonResult;
import com.lyd.springcloud.alibaba.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Liuyunda
 * @Date 2020/12/22 0:23
 * @Email man021436@163.com
 * @Description: DOTO
 */
@RestController
public class StorageController {
    @Autowired
    private StorageService storageService;

    /**
     * @Description: 扣减库存
     * @Param: [productId, count]
     * @return: com.lyd.springcloud.alibaba.domain.CommonResult
     * @Author: Liuyunda
     * @Date: 2020/12/22
     */
    @RequestMapping("/storage/decrease")
    public CommonResult decrease(Long productId, Integer count) {
        storageService.decrease(productId, count);
        return new CommonResult(200,"扣减库存成功！");
    }
}
