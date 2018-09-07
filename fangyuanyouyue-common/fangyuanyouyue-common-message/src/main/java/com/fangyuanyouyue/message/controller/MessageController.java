package com.fangyuanyouyue.message.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.util.CheckCode;
import com.fangyuanyouyue.message.param.EaseMobParam;
import com.fangyuanyouyue.message.param.MessageParam;
import com.fangyuanyouyue.message.service.MessageService;
import com.fangyuanyouyue.message.service.SchedualUserService;
import com.fangyuanyouyue.message.service.impl.EasemobIMUsers;
import com.fangyuanyouyue.message.service.impl.EasemobSendMessage;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.client.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private SchedualUserService schedualUserService;

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
			@ApiImplicitParam(name = "type", value = "消息类型 1系统消息 2交易消息 3社交消息 4新增粉丝 5邀请我", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "jumpType", value = "跳转类型", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "businessId", value = "业务ID，如订单消息的订单ID", required = true, dataType = "string", paramType = "query")
//			@ApiImplicitParam(name = "nickName", value = "昵称", required = true, dataType = "string", paramType = "query"),
//			@ApiImplicitParam(name = "headImgUrl", value = "头像url", required = true, dataType = "string", paramType = "query")
	})
    //环信发送普通消息
    @PostMapping(value = "/easemob/message")
    @ResponseBody
    public BaseResp easemobMessage(EaseMobParam param) throws IOException {
        try {
            log.info("----》环信发送普通消息《----");
            log.info("参数："+param.toString());
//            UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(Integer.valueOf(param.getUserName()))).getString("data")), UserInfo.class);
            String headImgUrl = "http://pic.baike.soso.com/p/20120419/20120419170638-1137484758.jpg";
            String nickName;
            String from;
            if("1".equals(param.getJumpType())){
                nickName = "系统消息";
                from = "65";
//                headImgUrl = "";
            }else if("2".equals(param.getJumpType())){
                nickName = "交易消息";
                from = "66";
//                headImgUrl = "";
            }else if("3".equals(param.getJumpType())){
                nickName = "社交消息";
                from = "67";
//                headImgUrl = "";
            }else if("4".equals(param.getJumpType())){
                nickName = "新增粉丝";
                from = "68";
//                headImgUrl = "";
            }else{
                nickName = "邀请我";
                from = "69";
//                headImgUrl = "";
            }
            Msg msg = new Msg();
            MsgContent msgContent = new MsgContent();
            msgContent.type(MsgContent.TypeEnum.TXT).msg(param.getContent());
            UserName userName = new UserName();
            userName.add(param.getUserName());
            Map<String,Object> ext = new HashMap<>();
            ext.put("nickName", nickName);//默认为系统
            ext.put("headImgUrl",headImgUrl);//默认为系统头像
            ext.put("jumpType",param.getJumpType());
            ext.put("businessId",param.getBusinessId());
            /*
                from        :来源
                target      :目标
                targetType  :目标类型
                msg         :信息内容
                ext         :额外信息
             */
            msg.from(from).target(userName).targetType("users").msg(msgContent).ext(ext);
            System.out.println("msg:"+new GsonBuilder().create().toJson(msg));
            Object result = easemobSendMessage.sendMessage(msg);
            //TODO 判断消息是否成功

//    		log.info("判断消息是否成功------:"+result);

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
            log.info("----》发送微信模版消息《----");
            log.info("参数："+param.toString());
            
            Msg msg = new Msg();
            MsgContent msgContent = new MsgContent();
            msgContent.type(MsgContent.TypeEnum.TXT).msg(param.getContent());
            UserName userName = new UserName();
            userName.add(param.getUserName());
            Map<String,Object> ext = new HashMap<>();
            ext.put("nickName", "小方圆官方");
            ext.put("headImgUrl",param.getHeadImgUrl());
            ext.put("type",param.getType());
            ext.put("businessId",param.getBusinessId());
            msg.from("system").target(userName).targetType("users").msg(msgContent).ext(ext);
            System.out.println("msg:"+new GsonBuilder().create().toJson(msg));
            Object result = easemobSendMessage.sendMessage(msg);
            //TODO 判断消息是否成功
            log.info("判断消息是否成功------:"+result);

            return toSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
    
}
