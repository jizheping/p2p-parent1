package com.jizheping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Zuul网关服务启动类
 */

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class GatewayZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayZuulApplication.class,args);
    }
}
