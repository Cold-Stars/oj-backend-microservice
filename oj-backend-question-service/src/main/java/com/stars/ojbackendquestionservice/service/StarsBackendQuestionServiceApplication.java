package com.stars.ojbackendquestionservice.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@MapperScan("com.stars.ojbackendquestionservice.service.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.stars")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.stars.ojbackendservicclient.service"})
public class StarsBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarsBackendQuestionServiceApplication.class, args);
    }

}
