package com.fangyuanyouyue.message.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.util.CheckCode;
import com.fangyuanyouyue.message.param.MessageParam;
import com.fangyuanyouyue.message.service.MessageService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/message")
@Api(description = "消息系统Controller")
@RefreshScope
public class MessageController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private MessageService messageService;

    @PostMapping(value = "/sendCode")
    @ResponseBody
    public BaseResp sendCode(MessageParam param) throws IOException {
        try {
            log.info("----》发送验证码《----");
            log.info("参数："+param.toString());

            String code = CheckCode.getCheckCode();
            messageService.sendCode(param.getPhone(), code, "");

            return toSuccess(code);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

}
