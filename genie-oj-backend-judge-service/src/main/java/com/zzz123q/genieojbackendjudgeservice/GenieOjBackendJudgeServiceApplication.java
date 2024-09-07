package com.zzz123q.genieojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.zzz123q.genieojbackendjudgeservice.message.InitRabbitMq;

@SpringBootApplication
@ComponentScan("com.zzz123q")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.zzz123q.genieojbackendserviceclient.service" })
public class GenieOjBackendJudgeServiceApplication {

    public static void main(String[] args) {
        // 启动消息队列
        InitRabbitMq.doInit();
        SpringApplication.run(GenieOjBackendJudgeServiceApplication.class, args);
    }

}
