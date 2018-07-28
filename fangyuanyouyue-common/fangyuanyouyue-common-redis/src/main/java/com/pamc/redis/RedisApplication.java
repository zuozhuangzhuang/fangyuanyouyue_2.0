package com.pamc.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class RedisApplication {

  // main函数，Spring Boot程序入口
  public static void main(String[] args) {
    SpringApplication.run(RedisApplication.class, args);
  }

}


