package com.fangyuanyouyue.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
@Configuration
@EnableSwagger2
public class ZuulServerApplication {

	/**
	 * ************************************************************************
	 * **                              _oo0oo_                               **
	 * **                             o8888888o                              **
	 * **                             88" . "88                              **
	 * **                             (| -_- |)                              **
	 * **                             0\  =  /0                              **
	 * **                           ___/'---'\___                            **
	 * **                        .' \\\|     |// '.                          **
	 * **                       / \\\|||  :  |||// \\                        **
	 * **                      / _ ||||| -:- |||||- \\                       **
	 * **                      | |  \\\\  -  /// |   |                       **
	 * **                      | \_|  ''\---/''  |_/ |                       **
	 * **                      \  .-\__  '-'  __/-.  /                       **
	 * **                    ___'. .'  /--.--\  '. .'___                     **
	 * **                 ."" '<  '.___\_<|>_/___.' >'  "".                  **
	 * **                | | : '-  \'.;'\ _ /';.'/ - ' : | |                 **
	 * **                \  \ '_.   \_ __\ /__ _/   .-' /  /                 **
	 * **            ====='-.____'.___ \_____/___.-'____.-'=====             **
	 * **                              '=---='                               **
	 * ************************************************************************
	 * **                        佛祖保佑      启动正常                      **
	 * ************************************************************************
	 *
	 */

	public static void main(String[] args) {
		SpringApplication.run(ZuulServerApplication.class, args);
	}

	/**
	 * 文件上传配置
	 *
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//  单个数据大小
		factory.setMaxFileSize("1024MB"); // KB,MB
		/// 总上传数据大小
		factory.setMaxRequestSize("10240MB");
		return factory.createMultipartConfig();
	}
}
