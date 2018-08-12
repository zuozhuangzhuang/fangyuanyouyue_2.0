package com.fangyuanyouyue.message.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import com.fangyuanyouyue.message.param.EaseMobParam;
import com.fangyuanyouyue.message.param.MessageParam;
import com.fangyuanyouyue.message.service.MessageService;
import com.fangyuanyouyue.message.service.impl.EasemobIMUsers;
import com.fangyuanyouyue.message.service.impl.EasemobSendMessage;
import com.google.gson.GsonBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.client.model.Msg;
import io.swagger.client.model.MsgContent;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;
import io.swagger.client.model.UserName;

@RestController
@RequestMapping(value = "/message")
@Api(description = "消息系统Controller")
@RefreshScope
public class MessageController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private EasemobIMUsers easemobIMUsers;

    @Autowired
    private EasemobSendMessage easemobSendMessage;

	@ApiOperation(value = "发送验证码", notes = "发送验证码", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "phone", value = "手机号码", required = true, dataType = "string", paramType = "query")
	})
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
	
	@ApiOperation(value = "环信注册", notes = "注册环信账号", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userName", value = "环信账号", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "password", value = "环信登录密码", required = true, dataType = "string", paramType = "query")
	})
    //环信注册
    @PostMapping(value = "/easemob/regist")
    @ResponseBody
    public BaseResp easemobRegist(EaseMobParam param) throws IOException {
        try {
            log.info("----》环信注册用户《----");
            log.info("参数："+param.toString());

            RegisterUsers users = new RegisterUsers();
            User user = new User().username(param.getUserName()).password(param.getPassword());
            users.add(user);
    		Object object = easemobIMUsers.createNewIMUserSingle(users);
    		log.info(object);
    		if(object!=null) {
                return toSuccess(object);
    		}
    		return toError("注册失败");

        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
	
	@ApiOperation(value = "环信消息", notes = "发送环信信息", response = BaseResp.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userName", value = "环信账号", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "content", value = "消息内容", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "消息类型", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "businessId", value = "业务ID，如订单消息的订单ID", required = true, dataType = "string", paramType = "query")
	})
    //环信发送普通消息
    @PostMapping(value = "/easemob/message")
    @ResponseBody
    public BaseResp easemobMessage(EaseMobParam param) throws IOException {
        try {
            log.info("----》环信注册用户《----");
            log.info("参数："+param.toString());
            
            Msg msg = new Msg();
            MsgContent msgContent = new MsgContent();
            msgContent.type(MsgContent.TypeEnum.TXT).msg(param.getContent());
            UserName userName = new UserName();
            userName.add(param.getUserName());
            Map<String,Object> ext = new HashMap<>();
            ext.put("nickName","小方圆官方");//默认为系统
            ext.put("headImgUrl",param.getHeadImgUrl());//默认为系统头像
            ext.put("type",param.getType());
            ext.put("businessId",param.getBusinessId());
            msg.from("system").target(userName).targetType("users").msg(msgContent).ext(ext);
            System.out.println(new GsonBuilder().create().toJson(msg));
            Object result = easemobSendMessage.sendMessage(msg);
            //TODO 判断消息是否成功
            
    		log.info(result);

            return toSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    //发送微信模版消息
    @PostMapping(value = "/wechat/message")
    @ResponseBody
    public BaseResp wechatMessage(EaseMobParam param) throws IOException {
        try {
            log.info("----》环信注册用户《----");
            log.info("参数："+param.toString());
            
            Msg msg = new Msg();
            MsgContent msgContent = new MsgContent();
            msgContent.type(MsgContent.TypeEnum.TXT).msg(param.getContent());
            UserName userName = new UserName();
            userName.add(param.getUserName());
            Map<String,Object> ext = new HashMap<>();
            ext.put("nickName",param.getNickName());
            ext.put("headImgUrl",param.getHeadImgUrl());
            ext.put("type",param.getType());
            ext.put("businessId",param.getBusinessId());
            msg.from("system").target(userName).targetType("users").msg(msgContent).ext(ext);
            System.out.println(new GsonBuilder().create().toJson(msg));
            Object result = easemobSendMessage.sendMessage(msg);
            //TODO 判断消息是否成功
    		log.info(result);

            return toSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
    
}
