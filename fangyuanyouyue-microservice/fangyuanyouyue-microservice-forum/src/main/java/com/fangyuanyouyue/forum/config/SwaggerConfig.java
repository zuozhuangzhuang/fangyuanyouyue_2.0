package com.fangyuanyouyue.forum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger配置类
 * 用@Configuration注解该类，等价于XML中配置beans；用@Bean标注方法等价于XML中配置bean。
 * Application.class 加上注解@EnableSwagger2 表示开启Swagger
 */


// @Api：用在类上，说明该类的作用
// @ApiOperation：用在方法上，说明方法的作用
// @ApiImplicitParams：用在方法上包含一组参数说明
// @ApiImplicitParam：用在 @ApiImplicitParams 注解中，指定一个请求参数的各个方面
// paramType：参数放在哪个地方
// · header --> 请求参数的获取：@RequestHeader
// · query -->请求参数的获取：@RequestParam
// · path（用于restful接口）--> 请求参数的获取：@PathVariable
// · body（不常用）
// · form --> 提交数据时使用
// name：参数名
// dataType：参数类型
// required：参数是否必须传
// value：参数的意思
// defaultValue：参数的默认值
// @ApiResponses：用于表示一组响应
// @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
// code：数字，例如400
// message：信息，例如"请求参数没填好"
// response：抛出异常的类
// @ApiModel：描述一个Model的信息（这种一般用在post创建的时候，使用@RequestBody这样的场景，请求参数无法使用@ApiImplicitParam注解进行描述的时候）
// @ApiModelProperty：描述一个model的属性
// contact：name 作者 email 作者邮箱地址
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fangyuanyouyue.forum.controller"))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("小方圆论坛系统api")
                .description("论坛系统接口文档说明")
                .contact(new Contact("左壮壮", "", "zuozhuang_zzz@163.com"))
                .version("1.0")
                .build();
    }

}
