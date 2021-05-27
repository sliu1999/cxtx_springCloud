package com.cxtx.auth_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.cxtx.**.mapper")
public class AuthServiceApplication {

    public static void main(String [] args){

        SpringApplication.run(AuthServiceApplication.class,args);
    }

}
