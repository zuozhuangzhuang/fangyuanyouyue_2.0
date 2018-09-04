package com.fangyuanyouyue.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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

	/**
	 *
	 * attention:简单跨域就是GET，HEAD和POST请求，但是POST请求的"Content-Type"只能是application/x-www-form-urlencoded, multipart/form-data 或 text/plain
	 * 反之，就是非简单跨域，此跨域有一个预检机制，说直白点，就是会发两次请求，一次OPTIONS请求，一次真正的请求
	 */
	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); // 允许cookies跨域
		config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
		config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
		config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
		config.addAllowedMethod("OPTIONS");// 允许提交请求的方法，*表示全部允许
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");// 允许Get的请求方法
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
