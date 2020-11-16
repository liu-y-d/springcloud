package com.lyd.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Liuyunda
 * @Date 2020/11/16 22:10
 * @Email man021436@163.com
 * @Description: DOTO
 */
@Configuration
public class GatewayConfig {

    /**
     * @Description: 配置了一个id为route-name的路由规则
     * 当访问http://localhost:9527/guonei时自动转发到地址http://news.baidu.com/guonei
     * @Param: [routeLocatorBuilder]
     * @return: org.springframework.cloud.gateway.route.RouteLocator
     * @Author: Liuyunda
     * @Date: 2020/11/16
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("route_lyd1",
                r -> r.path("/guonei")
                        .uri("http://news.baidu.com/guonei")).build();
        return routes.build();
    }
}
