package com.stars.ojbackendjudgeservice;

import com.stars.ojbackendjudgeservice.rabbitmq.InitRabbitMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.stars")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.stars.ojbackendservicclient.service"})
public class StarsBackendJudgeServiceApplication {

    public static void main(String[] args) {
        //启动消息队列
        InitRabbitMq.doInit();

        SpringApplication.run(StarsBackendJudgeServiceApplication.class, args);
    }

}
