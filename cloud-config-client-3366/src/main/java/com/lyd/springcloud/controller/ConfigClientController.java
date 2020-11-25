package com.lyd.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Liuyunda
 * @Date 2020/11/25 21:36
 * @Email man021436@163.com
 * @Description: DOTO
 */
@RestController
@RefreshScope
public class ConfigClientController {
    @Value("${server.port}")
    private String serverPort;

    @Value("${config.info}")
    private String configInfo;
    @GetMapping("/configInfo")
    public String configInfo(){
        return "serverPort:"+serverPort+"\t\n\n configInfo: "+configInfo;
    }
}
