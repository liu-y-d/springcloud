package com.lyd.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/testD")
    public String testD(){
        // try {
        //     TimeUnit.SECONDS.sleep(1);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        log.info("----testD 异常比例");
        int age = 10/0;
        return "-------testD";
    }
    @GetMapping("/testE")
    public String testE(){

        log.info("----testE 异常数");
        int age = 10/0;
        return "-------testE";
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "deal_testHotKey")// 如果不配置blockHandler则会跳到error page
    public String testHotKey(@RequestParam(value = "p1",required = false)String p1,@RequestParam(value = "p2",required = false)String p2){
        int age = 10/0;
        return "----testHotKey";
    }
    public String deal_testHotKey(String p1, String p2, BlockException exception){
        return "----testHotKey,兜底";
    }
}
