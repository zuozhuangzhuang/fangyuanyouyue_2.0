package com.fangyuanyouyue.forum;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@RefreshScope
@EnableFeignClients
public class ForumServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumServiceApplication.class, args);
	}


	private static final Logger LOG = Logger.getLogger(ForumServiceApplication.class.getName());
	@Value("${server.port}")
	String port;
	@RequestMapping("/forum")
	public String forum(@RequestParam String name) {
		LOG.log(Level.INFO, "calling trace business-system  ");
		return "forum "+name+",i am from port:" +port;
	}
}
