package com.zzz123q.genieojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.zzz123q.genieojbackenduserservice.mapper")
@ComponentScan("com.zzz123q")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GenieOjBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenieOjBackendUserServiceApplication.class, args);
    }

}
