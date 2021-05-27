package com.cxtx.user_manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.cxtx.*"})
@EnableFeignClients
@MapperScan("com.cxtx.**.mapper")
@EnableAutoConfiguration
public class UserManageApplication {

    public static void main(String [] args){

        SpringApplication.run(UserManageApplication.class,args);
    }

}