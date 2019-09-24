package com.jizheping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@MapperScan(basePackages = "com.jizheping.mapper")
public class BidServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BidServiceApplication.class,args);
    }
}
