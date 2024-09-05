package com.zzz123q.genieojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GenieOjBackendGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenieOjBackendGatewayApplication.class, args);
    }

}
