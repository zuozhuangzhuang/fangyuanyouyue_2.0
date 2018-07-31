package com.pamc.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableEurekaClient
@RefreshScope
@EnableFeignClients
public class RedisApplication {

  // main函数，Spring Boot程序入口
  public static void main(String[] args) {
    SpringApplication.run(RedisApplication.class, args);
  }

}


