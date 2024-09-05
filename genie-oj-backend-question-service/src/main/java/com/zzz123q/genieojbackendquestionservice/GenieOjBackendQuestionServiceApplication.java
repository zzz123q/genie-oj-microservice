package com.zzz123q.genieojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.zzz123q.genieojbackendquestionservice.mapper")
@ComponentScan("com.zzz123q")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GenieOjBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenieOjBackendQuestionServiceApplication.class, args);
    }

}
