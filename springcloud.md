# SpringCloud笔记

## 1.父项目构建

### 1.1 dependencyManagement和dependencies区别

dependencyManagement能让所有在子项目中引用一个依赖而不用显式的列出版本号，maven会沿着父子层次向上走知道找到一个拥有dependencyManagement元素的项目，然后他就会使用这个dependencyManagement元素中的版本号。

dependencyManagement只是生命依赖，并不实现引入。

## 2.Eureka

### 2.1什么是服务治理

SpringCloud封装了NetFlix公司开发的Eureka模块来是脸服务治理

传统RPC远程调用框架中，管理每个服务与服务之间依赖关系比较复杂，管理比较复杂，所以需要使用服务治理，管理服务于服务之间依赖关系，可以实现服务调用、负载均衡、容错等，实现服务发现与注册。

### 2.2什么是服务注册与发现

Eureka采用了CS的设计架构，EurekaServer作为服务注册功能的服务器，它是服务注册中心。而系统中的其他微服务，使用Eureka的客户端链接到EurekaServer并维持心跳连接。这样系统的维护人员就可以通过EurekaServer来监控系统中各个微服务是否运行。

在服务注册与发现中，有一个注册中心。当服务器启动的时候，会把当前自己服务器的信息，比如服务地址通讯地址等以别名的方式注册到注册中心中，另一方（消费者|服务提供者），以该别名的方式去注册中心上获取到实际的服务通讯地址，然后再实现本地RPC调用RPC远程调用框架核心设计思想：在于注册中心，因为使用注册中心管理每个服务与服务之间的一个依赖关系（服务治理概念）。在任何RPC远程框架中，都会有一个注册中心（存放服务地址相关信息（接口地址））。

### 2.3Eureka的两个组件Eureka Server和EurekaClient

- EurekaServer提供服务注册服务

  各个微服务节点通过配置启动后，会在EurekaServer中进行注册，这样EurekaServer中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观看到。

- EurekaClient通过注册中心进行访问

  是一个Java客户端，用与简化EurekaServer的交互，客户端同时也具备一个内置的、使用轮询（round-robin）负载算法的负载均衡器。在应用启动后，将会向EurekaServer发送心跳（默认周期为30 秒）。如果EurekaServer在多个心跳周期内没有接收到某个节点的心跳，EurekaServer将会从服务注册表中把这个服务节点移除（默认90秒）。

### 2.4单机Eureka构建步骤

1. idea生成EurekaServer端服务注册中心（物业公司）
2. EurekaClient端cloud-provider-payment8001，将要注册进EurekaServer成为服务提供者provider（尚硅谷学校对外提供授课服务）
3. EurekaClient端cloud-consumer-order80，将要注册进EurekaServer成为服务消费者consumer（尚硅谷上课的同学）

### 2.5集群Eureka构建步骤

1. Eureka集群原理说明（互相注册，相互守望）

   1. 先启动Eureka注册中心

   2. 启动服务提供者payment支付服务

   3. 支付服务启动后会把自身信息（比如服务地址以别名方式注册进Eureka）

   4. 消费者order服务在需要调用接口时，使用服务别名去注册中心获取实际的Rpc远程调用地址

   5. 消费者获得地址后底层实际是利用HTTPClient技术实现远程调用

   6. 消费者获得服务地址后会缓存在本地jvm内存中，默认每间隔30秒更新一次服务调用地址

      **问题**：微服务RPC远程服务调用最核心的是什么

      **回答**：高可用，试想你的注册中心只有一个，他出故障了，就会导致整个微服务不可用。**所以采用**搭建Eureka注册中心集群，实现负载均衡+故障容错

2. EurekaServer集群环境构建步骤

   1. 参考7001
   2. 新建7002
   3. 改POM
   4. 修改映射配置
   5. 写YML
   6. 朱启东

3. 将支付服务8001微服务发布到上面2台Eureka集群配置中

4. 将订单服务80微服务发布到上面2台Eureka集群配置中

5. 测试01

   1. 先启动7001、7002
   2. 在启动8001、80

6. 支付服务提供者8001集群环境构建

   参考8001新建8002

7. 负载均衡

   - 使用@LoadBalanced注解赋予RestTemplate负载均衡的能力
   - ![image-20200802215826490](https://gitee.com/SexJava/FigureBed/raw/master/static/image-20200802215826490.png)

8. 测试02

   1. 先启动EurekaServer，7001、7002
   2. 在启动EurekaClient,8001/8002
   3. 在启动80
   4. localhost/consumer/payment/get/1
   5. 结果（轮询）
      - ![image-20200802220151548](https://gitee.com/SexJava/FigureBed/raw/master/static/image-20200802220151548.png)
      - ![image-20200802220218018](https://gitee.com/SexJava/FigureBed/raw/master/static/image-20200802220218018.png)
   6. Ribbon和Eureka整合后Consumer可以直接调用服务而不用再关心地址和端口号，且该服务还具备负载均衡功能
   
9. 修改主机名称

   ```yml
   eureka:
     client:
       register-with-eureka: true #表示是否将自己注册进EurekaServer默认为true
       fetch-registry: true #是否从EurekaServer抓取已有的注册信息，默认为true，单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
       service-url:
   #      defaultZone: http://localhost:7001/eureka
         defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka #集群版
     #添加实例id修改主机名称
     instance:
       instance-id: payment8001
   ```

10. 提示信息有ip提示

    ```yml
    eureka:
      client:
        register-with-eureka: true #表示是否将自己注册进EurekaServer默认为true
        fetch-registry: true #是否从EurekaServer抓取已有的注册信息，默认为true，单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
        service-url:
          #      defaultZone: http://localhost:7001/eureka
          defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka #集群版
      instance:
        instance-id: payment8002
        prefer-ip-address: true #访问路径可以显示ip地址
    ```

    

### 2.6服务发现Discovery

1. 对于注册进Eureka里面的微服务，可以通过服务发现来获得该服务的信息

2. 修改cloud-provider-payment8001的Controller

   ```java
   @GetMapping(value = "/payment/discovery")
       public Object discovery(){
           List<String> services = discoveryClient.getServices();
           for (String service: services) {
               log.info("---element:"+service);
           }
           List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
           for (ServiceInstance instance : instances) {
               log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
           }
           return this.discoveryClient;
       }
   ```

   

3. 8001主启动类

   ```java
   @SpringBootApplication
   @EnableEurekaClient
   @EnableDiscoveryClient
   public class PaymentMain8001 {
       public static void main(String[] args) {
           SpringApplication.run(PaymentMain8001.class,args);
       }
   }
   ```

   

4. 自测  

   ![image-20200803202017324](https://gitee.com/SexJava/FigureBed/raw/master/static/image-20200803202017324.png)

### 2.7Eureka自我保护

1. 故障现象

   保护模式主要用于一组客户端和EurekaServer之间存在网络分区场景下的保护。一旦进入保护模式，**EurekaServer将会尝试保护其服务注册表中的信息，不再删除服务注册表中的数据，也就是不会注销任何微服务。**

   如果在EurekaServer的首页看到这段提示，则说明Eureka进入了保护模式

   #### **EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.**

2. 导致原因

   某时刻某一个微服务不可用了，Eureka不会立刻清理，依旧会对微服务的信息进行保存

   属于CAP中的AP分支

3. 怎么禁止自我保护

   ```yml
   server:
     port: 7001
   eureka:
     instance:
       hostname: eureka7001.com #eureka服务端的实例名称
     client:
       register-with-eureka: false #false标识不向注册中心注册自己
       fetch-registry: false #false标识自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
       service-url:
         defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/ #设置与EurekaServer交互的地址查询服务和注册服务都需要依赖这个地址
   #      defaultZone: http://eureka7002.com:7002/eureka/ #设置与EurekaServer交互的地址查询服务和注册服务都需要依赖这个地址
     server:
       enable-self-preservation: false #关闭自我保护机制，保证不可用服务被及时剔除
       eviction-interval-timer-in-ms: 2000
   ```

   ```yml
   server:
     port: 8001
   
   
   spring:
     application:
       name: cloud-payment-service
     datasource:
       type: com.alibaba.druid.pool.DruidDataSource  # 当前数据源操作类型
       driver-class-name: com.mysql.cj.jdbc.Driver # mysql 驱动包
       url: jdbc:mysql://localhost:3306/db2020?useUnicode=true&characterEncoding=utf-8
       username: root
       password: 123456
   eureka:
     client:
       register-with-eureka: true #表示是否将自己注册进EurekaServer默认为true
       fetch-registry: true #是否从EurekaServer抓取已有的注册信息，默认为true，单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
       service-url:
         defaultZone: http://localhost:7001/eureka
   #      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka #集群版
     instance:
       instance-id: payment8001
       prefer-ip-address: true #访问路径可以显示ip地址
       lease-renewal-interval-in-seconds: 1 #Eureka客户端向服务端发送心跳的时间间隔，单位秒（默认30）
       lease-expiration-duration-in-seconds: 2 #Eureka服务端在收到最后一次心跳后等待时间上线，单位秒（默认90秒），超时将剔除服务
   mybatis:
     mapper-locations: classpath:mapper/*.xml
     type-aliases-package: com.lyd.springcloud.entities # 所有Entity别名类所在包
   
   ```

   

## 3.Zookeeper服务注册与开发

SpringCloud整合Zookeeper代替Eureka

### 3.1注册中心Zookeeper

1. zookeeper是一个分布式协调工具，可以实现注册中心功能

2. 关闭Linux服务器防火墙后启动zookeeper服务器

3. zookeeper服务器取代Eureka服务器，zookeeper作为服务注册中心

4. Linux安装

   [zookeeper]: https://www.cnblogs.com/zhiyouwu/p/11546097.html	"zookeeper"

   

### 3.2服务提供者

1. 新建cloud-provider-payment8004
2. POM
3. YML
4. 主启动类
5. controller
6. 启动8004注册进zookeeper
7. 验证测试
8. 验证测试2
9. 思考
   1. 服务节点是临时节点还是持久节点（临时）

### 3.3服务消费者

## 4.Consul服务注册与发现

### 4.1Consul简介

1. 是什么

   一套开源的分布式服务发现和配置管理系统，用Go语言开发，提供了微服务系统中的服务治理、配置中心、控制总线等功能。这些功能中的每一个都可以根据需要单独使用，也可以一起使用以构建全方位的服务网格，总之Consul提供了一种完整的服务网格解决方案。它具有很多优点，包括：基于raft协议，比较简洁；支持健康检查，同时支持HTTP和DNS协议支持跨数据中心的WAN集群提供土星界面跨平台，支持Linux，Mac，Windows

2. 能干嘛

   1. 服务注册与发现（支持HTTP和DNS两种发现方式）
   2. 健康检查（支持多种方式，HTTP、TCP、Docker、Shell脚本定制化）
   3. KV键值对存储
   4. 多数据中心（Consul支持多数据中心）
   5. 可视化Web界面

3. 去哪下

   1. https://pan.baidu.com/s/1nMXRms3_ZPhgqawrcEcsdA 提取码 247m

4. 怎么玩

### 4.2安装并运行Consul

1. 双击运行exe程序，安装成功
2. consul agent -dev开发模式运行
3. 访问localhost:8500

### 4.3服务提供者

1. 新建module支付服务provider8006
2. pom
3. yml
4. 主启动类
5. 业务类Controller
6. 验证测试

### 4.4服务消费者

1. 新建module消费服务order80（cloud-consumerconsul-order80）
2. pom
3. yml
4. 主启动类
5. 配置bean
6. controller
7. 验证测试
8. 访问测试地址

### 4.5三个注册中心异同点

|  组件名   | 语言 |      CAP       | 服务健康检查 | 对外暴露接口 | SpringCloud集成 |
| :-------: | :--: | :------------: | :----------: | ------------ | :-------------: |
|  Eureka   | Java |  AP（高可用）  |   可配支持   | HTTP         |     已集成      |
| Zookeeper | Java | CP（数据一致） |     支持     | 客户端       |     已集成      |
|  Consul   |  Go  | CP（数据一致） |     支持     | HTTP/DNS     |     已集成      |

- CAP
  - C：Consistency（强一致性）
  - A：Availability（可用性）
  - P：Parttiton tolerance（分区容错性）
  - CAP理论关注力度是数据，而不是整体系统设计的策略
  - CAP理论的核心是：一个分布式系统不可能同时很好的满足一致性，可用性和分区容错性这三个需求，因此，根据CAP原理将NoSQL数据库分成了满足CA原则、满足CP原则和满足AP原则三大类
    - CA-单点集群，满足一致性，可用性的系统，通常在可扩展性上不太强大
    - CP-满足一致性，分区容忍性的系统，通畅性能不是特别高
    - AP-满足可用性，分区容忍性的系统，通常可能对一致性要求低一些