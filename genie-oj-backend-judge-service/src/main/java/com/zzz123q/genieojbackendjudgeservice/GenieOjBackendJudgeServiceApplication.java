package com.zzz123q.genieojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("com.zzz123q")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GenieOjBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenieOjBackendJudgeServiceApplication.class, args);
    }

}
