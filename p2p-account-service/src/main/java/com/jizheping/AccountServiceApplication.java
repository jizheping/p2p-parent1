package com.jizheping;

import com.jizheping.api.util.RedisUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * 账户信息启动类
 */

@EnableEurekaClient
@SpringBootApplication
@ComponentScan(value = "com.jizheping",excludeFilters = {
        //根据注解进行排除
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = {RedisUtil.class})
})
@MapperScan(basePackages = "com.jizheping.mapper")
public class AccountServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class,args);
    }
}
