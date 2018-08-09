package com.fangyuanyouyue.zuul.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
//        resources.add(swaggerResource("消息系统", "/message/v2/api-docs", "1.0"));
//        resources.add(swaggerResource("权限系统", "/auth/v2/api-docs", "1.0"));
        resources.add(swaggerResource("商品系统", "/goods/v2/api-docs", "1.0"));
        resources.add(swaggerResource("论坛系统", "/forum/v2/api-docs", "1.0"));
        resources.add(swaggerResource("订单系统", "/order/v2/api-docs", "1.0"));
//        resources.add(swaggerResource("支付系统", "/payment/v2/api-docs", "1.0"));
        resources.add(swaggerResource("钱包系统", "/wallet/v2/api-docs", "1.0"));
        resources.add(swaggerResource("用户系统", "/user/v2/api-docs", "1.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
