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

## 5.Ribbon负载均衡服务调用

### 5.1概述

1. 是什么

   SpringCloudRibbon是基于NetFlixRibbon实现的一套客户端 负载均衡工具

   简单到说，Ribbon是Netflix发布的开源项目，主要功能是提供客户端的软件负载均衡算法和服务调用。Ribbon客户端组件提供一系列完善的配置如连接超时，重试等。简单的说，就是在配置文件中列出LoadBalancer（简称LB）后面所有的机器，Ribbon会祖东的帮助你基于某种规则（简单轮询，随机链接等）去链接这些机器。我们很容易使用Ribbon实现自定义的负载均衡算法。

2. 能干嘛

   1. LB负载均衡

      简单的说局势将用户的请求平摊的分配到多个服务上，从而达到系统的HA(高可用)，常见的负载均衡软件有Nginx，LVS，硬件F5等

      Ribbon本地负载均衡客户端VSNginx服务端负载均衡区别

      ​	Nginx是服务器负载均衡，客户端所有请求都会交个Nginx，然后由Nginx实现转发起请求。即负载均衡是由服务端实现的

      ​	Ribbon本地负载均衡，在调用微服务接口时候，会在注册中心上获取注册信息服务列表之后缓存搞JVM本地，从而本地实现RPC远程服务调用技术

      1. 集中式LB（Nginx，F5）
      2. 进程内LB（Ribbon）

3. 一句话

   负载均衡+RestTemplate调用

### 5.2Ribbon负载均衡演示

### 5.3Ribbon核心组件IRule

1. IRule:根据特定算法中从服务列表中选取一个要访问的服务

   ![image-20200807212832238](https://gitee.com/SexJava/FigureBed/raw/master/static/image-20200807212832238.png)

2. 如何替换

   1. 修改cloud-consumer-80
   2. 注意配置细节
   3. 新建package
   4. 上面包下新建MySelfRule规则类
   5. 主启动类添加@RibbonClient
   6. 测试

### 5.4Ribbon负载均衡算法

1. 原理（轮询）

   负载均衡算法：rest接口第几次请求数%服务器集群总数量 = 实际调用服务器位置下标

   ![image-20200816200306508](https://gitee.com/SexJava/FigureBed/raw/master/static/image-20200816200306508.png)

2. 源码

3. 手写



## 6.OpenFeign服务接口调用

Feign是一个声明式客户端。使用Feign能让编写WebService客户端更加简单。它的使用方法是定义一个服务接口然后在上面添加注解。Feign也支持可拔插式的编码器和解码器。SpringCloud对Feign进行了封装，使其支持了SpringMvc标准注解和HttpMessageConverters。Feign可以与Eureka和Ribbon组合使用以支持负载均衡。只需要创建一个接口并在接口上添加注解即可。

Feign能干什么

​	前面使用Ribbon和RestTemplate时们利用RestTemplate对Http请求的封装处理，形成了一套模板化的调用方法。但是在实际开发中，由于对服务依赖的调用可能不止一处，往往一个接口会被多处调用，所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用。所以Feign再次基础上做了进一步封装，由它来帮助我们定义和实现依赖服务接口的定义。在Feign的实现下，我们只需要创建一个接口并使用注解的方式配置它（以前是Dao接口上标注Mapper注解，现在是一个微服务接口上面标注一个Feign注解即可），即可完成对服务提供方的接口绑定，简化了使用SpringCloud Ribbon时，自动封装服务调用客户端的开发量。

Feign集成Ribbon

​	利用Ribbon维护了Payment的服务列表信息，别切通过轮询实现了客户端的负载均衡。而与Ribbon不同的是，通过Feign只需要定义服务绑定接口且以声明的方法，优雅而简单的实现了服务调用。

### 6.1OpenFeign使用步骤

1. 接口+注解（微服务调用接口+@FeignClient）

### 6.2OpenFeign超时设置

1. 超时设置，故意设置超时演示出错情况

   ![image-20200818214928684](https://gitee.com/SexJava/FigureBed/raw/master/static/image-20200818214928684.png)

2. OpenFeign默认等待1秒钟，超时后报错

3. 是什么

   默认Feign客户端只等待1秒钟，但是服务端处理需要超过1秒钟，导致Feign客户端不想等待了，直接返回报错。

   为了避免这样的情况，有时候我们需要设置Feign客户端的超时控制

4. YML文件里需要开启OpenFeign客户端超时控制

### 6.3OpenFeign日志打印功能

Feign提供了日志打印功能，我们可以通过配置来调整日志级别，从而了解Feign中Http请求的细节。说白了就是对Feign接口的调用情况进行监控和输出

1. 日志级别
   1. NONE：默认的，不显示任何日志
   2. BASIC：仅记录请求方法、URL、响应状态码及执行时间
   3. HEADERS：除了BASIC中定义的信息之外，还有请求和响应的头信息
   4. FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据

## 7.Hystrix 断路器

### 7.1概述

1. 分布式系统面临的问题

   复杂分布式体系结构中的应用程序有数十个依赖关系，每个依赖关系在某些时候将不可避免地失败。

   **服务雪崩**：

   多个微服务之间调用的时候，假设服务A调用服务B和服务C，微服务B和微服务C又调用其他的微服务，这就是所谓的扇出。如果扇出的链路上某个微服务的调用响应时间过长或者不可用，对微服务A的调用就会占用越来越多的系统资源，进而引起系统崩溃，所谓的雪崩效应。

2. 是什么

   Htystrix是一个用于处理分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，Hystrix能够保证在一个依赖出现问题的情况下，不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性。断路器本身是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控类似（熔断保险丝），向调用放返回一个符合预期的、可处理的备选响应（FallBack），而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间、不必要的占用、从而避免了故障在分布式系统中的蔓延乃至雪崩。

3. 能干嘛

   1. 服务降级
   2. 服务熔断
   3. 接近实时的监控 

### 7.2Hystrix重要概念

1. #### 服务降级（fallback）

   1. 服务器忙，请稍后再试，不让客户等待并立刻返回一个友好提示，fallback
   2. 哪些情况会触发降级
      1. 程序运行异常
      2. 超时
      3. 服务熔断触发服务降级
      4. 线程池/信号量打满也会导致服务降级

2. #### 服务熔断（break）

   1. 类比保险丝达到最大服务访问后，直接拒绝访问，拉闸限电，然后调用服务降级的方法并返回友好提示

   2. 就是保险丝-服务的降级->进而熔断->恢复调用链路

   3. 熔断类型

      1. 熔断打开：请求不再进行调用当前服务，内部设置时钟一般为MTTR（平均故障处理时间），当打开时长达到所设时钟则进入半熔断状态

      2. 熔断关闭：熔断关闭不会对服务进行熔断

      3. 熔断半开：部分请求根据规则调用当前服务，如果请求成功且符合规则则任务当前服务恢复正常，关闭熔断

         ```java
         /**
              * @Description: 服务熔断
              * enabled 是否开启断路器
              * requestVolumeThreshold 请求次数
              * sleepWindowInMilliseconds 时间窗口期
              * errorThresholdPercentage 失败率达到多少后跳闸（错误百分比阀值）
              * @Param: [id]
              * @return: java.lang.String
              * @Author: Liuyunda
              * @Date: 2020/11/15
              */
             @HystrixCommand(fallbackMethod = "paymentCircuitBreakerFallback",commandProperties = {
                     @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
                     @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
                     @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
                     @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),
             })
             public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
                 if (id < 0) {
                     throw new RuntimeException("*******id 不能为负数");
                 }
                 String serialNumber = IdUtil.simpleUUID();
                 return Thread.currentThread().getName() + "\t" + "调用成功，流水号：" + serialNumber;
             }
         
             public String paymentCircuitBreakerFallback(@PathVariable("id") Integer id) {
                 return "id 不能为负数，请稍后再试，id：" + id;
             }
         ```

      4. 参数解释

         1. **requestVolumeThreshold**（请求次数）

            在时间窗口期内，必须满足请求次数才有资格熔断。默认20，意味着在10秒内，如果该Hystrix命令的调用次数不足20次，及时所有的请求都超时或其他原因导致失败，断路器都不会打开

         2. **sleepWindowInMilliseconds**（时间窗口期）

            断路器确定是否打开需要统计一些请求和错误数据，而统计时间范围就是时间窗口期，默认为最近的10秒

         3. **errorThresholdPercentage**（错误百分比阀值）

            当请求次数在时间窗口期内超过了阀值，比如发生了30次调用，如果在这30次调用中，有15次发生了超时异常，也就是超过了50%的错误百分比，在默认设定的50%阀值情况下，这时候就会将断路器打开

      5. **断路器开启或关闭的条件**

         1. 当满足一定的阀值的时候（默认10秒内超过20个请求次数）
         2. 当失败率达到一定的时候（默认10秒内超过50%的请求失败）
         3. 达到以上阀值，断路器将会开启
         4. 当开启的时候，所有的请求都不会进行转发
         5. 一段时间后（默认是5秒），这个时候断路器是半开状态，会让其中一个请求进行转发。如果成功，断路器会关闭，如果失败，将重复步骤4和5.

3. #### 服务限流（flowlimit）

   1. 秒杀高并发等操作，严禁一蜂窝的过来拥挤，大家排队，一秒钟N个，有序进行



### 7.3Hystrix工作流程

### 7.4服务监控HystrixDashboard