package com.lyd.springcloud.alibaba.myhandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lyd.springcloud.entities.CommonResult;

/**
 * @Author Liuyunda
 * @Date 2020/12/16 23:12
 * @Email man021436@163.com
 * @Description: DOTO
 */
public class CustomerBlockHandler {

    public static CommonResult handlerException1(BlockException exception){
        return new CommonResult(4444,"按客户自定义,global handlerException----1");
    }
    public static CommonResult handlerException2(BlockException exception){
        return new CommonResult(4444,"按客户自定义,global handlerException----2");
    }
}
