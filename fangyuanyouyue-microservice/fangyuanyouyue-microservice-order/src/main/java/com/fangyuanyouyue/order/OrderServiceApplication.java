package com.fangyuanyouyue.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@RefreshScope
@EnableFeignClients
public class OrderServiceApplication {

	/**
	 * ---------神兽保佑 !---------
	 *
	 *   ┏┓      ┏┓
	 * ┏┛┻━━━┛┻┓
	 * ┃              ┃
	 * ┃      ━      ┃
	 * ┃  ┳┛  ┗┳  ┃
	 * ┃              ┃
	 * ┃      ┻      ┃
	 * ┃              ┃
	 * ┗━┓      ┏━┛
	 *     ┃      ┃
	 *     ┃      ┃
	 *     ┃      ┗━━━┓
	 *     ┃              ┣┓
	 *     ┃              ┏┛
	 *     ┗┓┓┏━┳┓┏┛
	 *       ┃┫┫  ┃┫┫
	 *       ┗┻┛  ┗┻┛
	 *
	 *
	 */

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
