package com.lyd.springcloud.alibaba.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Liuyunda
 * @Date 2020/12/15 22:28
 * @Email man021436@163.com
 * @Description: DOTO
 */
@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA(){

        // 阈值类型：线程数
        // try {
        //     TimeUnit.MILLISECONDS.sleep(800);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        return "-------testA";
    }

    @GetMapping("/testB")
    public String testB(){
        log.info(Thread.currentThread().getName()+"\t"+"----testB");
        return "-------testB";
    }
}
