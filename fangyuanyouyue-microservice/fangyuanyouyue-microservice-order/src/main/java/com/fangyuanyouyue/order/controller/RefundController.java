package com.fangyuanyouyue.order.controller;

import io.swagger.annotations.Api;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/refund")
@Api(description = "退货系统Controller")
@RefreshScope
public class RefundController {

}
