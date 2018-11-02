package com.fangyuanyouyue.message.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.CheckCode;
import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.message.model.UserInfo;
import com.fangyuanyouyue.message.model.WeChatMessage;
import com.fangyuanyouyue.message.param.EaseMobParam;
import com.fangyuanyouyue.message.param.MessageParam;
import com.fangyuanyouyue.message.service.MessageService;
import com.fangyuanyouyue.message.service.SchedualUserService;
import com.fangyuanyouyue.message.service.WeChatMessageService;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
    @Autowired
    private WeChatMessageService weChatMessageService;

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
            log.info("\n" +
                    " *               ii.                                         ;9ABH,          \n" +
                    " *              SA391,                                    .r9GG35&G          \n" +
                    " *              &#ii13Gh;                               i3X31i;:,rB1         \n" +
                    " *              iMs,:,i5895,                         .5G91:,:;:s1:8A         \n" +
                    " *               33::::,,;5G5,                     ,58Si,,:::,sHX;iH1        \n" +
                    " *                Sr.,:;rs13BBX35hh11511h5Shhh5S3GAXS:.,,::,,1AG3i,GG        \n" +
                    " *                .G51S511sr;;iiiishS8G89Shsrrsh59S;.,,,,,..5A85Si,h8        \n" +
                    " *               :SB9s:,............................,,,.,,,SASh53h,1G.       \n" +
                    " *            .r18S;..,,,,,,,,,,,,,,,,,,,,,,,,,,,,,....,,.1H315199,rX,       \n" +
                    " *          ;S89s,..,,,,,,,,,,,,,,,,,,,,,,,....,,.......,,,;r1ShS8,;Xi       \n" +
                    " *        i55s:.........,,,,,,,,,,,,,,,,.,,,......,.....,,....r9&5.:X1       \n" +
                    " *       59;.....,.     .,,,,,,,,,,,...        .............,..:1;.:&s       \n" +
                    " *      s8,..;53S5S3s.   .,,,,,,,.,..      i15S5h1:.........,,,..,,:99       \n" +
                    " *      93.:39s:rSGB@A;  ..,,,,.....    .SG3hhh9G&BGi..,,,,,,,,,,,,.,83      \n" +
                    " *      G5.G8  9#@@@@@X. .,,,,,,.....  iA9,.S&B###@@Mr...,,,,,,,,..,.;Xh     \n" +
                    " *      Gs.X8 S@@@@@@@B:..,,,,,,,,,,. rA1 ,A@@@@@@@@@H:........,,,,,,.iX:    \n" +
                    " *     ;9. ,8A#@@@@@@#5,.,,,,,,,,,... 9A. 8@@@@@@@@@@M;    ....,,,,,,,,S8    \n" +
                    " *     X3    iS8XAHH8s.,,,,,,,,,,...,..58hH@@@@@@@@@Hs       ...,,,,,,,:Gs   \n" +
                    " *    r8,        ,,,...,,,,,,,,,,.....  ,h8XABMMHX3r.          .,,,,,,,.rX:  \n" +
                    " *   :9, .    .:,..,:;;;::,.,,,,,..          .,,.               ..,,,,,,.59  \n" +
                    " *  .Si      ,:.i8HBMMMMMB&5,....                    .            .,,,,,.sMr \n" +
                    " *  SS       :: h@@@@@@@@@@#; .                     ...  .         ..,,,,iM5 \n" +
                    " *  91  .    ;:.,1&@@@@@@MXs.                            .          .,,:,:&S \n" +
                    " *  hS ....  .:;,,,i3MMS1;..,..... .  .     ...                     ..,:,.99 \n" +
                    " *  ,8; ..... .,:,..,8Ms:;,,,...                                     .,::.83 \n" +
                    " *   s&: ....  .sS553B@@HX3s;,.    .,;13h.                            .:::&1 \n" +
                    " *    SXr  .  ...;s3G99XA&X88Shss11155hi.                             ,;:h&, \n" +
                    " *     iH8:  . ..   ,;iiii;,::,,,,,.                                 .;irHA  \n" +
                    " *      ,8X5;   .     .......                                       ,;iihS8Gi\n" +
                    " *         1831,                                                 .,;irrrrrs&@\n" +
                    " *           ;5A8r.                                            .:;iiiiirrss1H\n" +
                    " *             :X@H3s.......                                .,:;iii;iiiiirsrh\n" +
                    " *              r#h:;,...,,.. .,,:;;;;;:::,...              .:;;;;;;iiiirrss1\n" +
                    " *             ,M8 ..,....,.....,,::::::,,...         .     .,;;;iiiiiirss11h\n" +
                    " *             8B;.,,,,,,,.,.....          .           ..   .:;;;;iirrsss111h\n" +
                    " *            i@5,:::,,,,,,,,.... .                   . .:::;;;;;irrrss111111\n" +
                    " *            9Bi,:,,,,......                        ..r91;;;;;iirrsss1ss1111\n" +
                    " ");
            log.info("----》环信发送普通消息《----");
            log.info("参数："+param.toString());
//            UserInfo user = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.parseObject(schedualUserService.verifyUserById(Integer.valueOf(param.getUserName()))).getString("data")), UserInfo.class);
//            String headImgUrl = "http://pic.baike.soso.com/p/20120419/20120419170638-1137484758.jpg";
            String headImgUrl;
            String nickName;
            String from;
            if("1".equals(param.getType())){
                nickName = "系统消息";
                from = "65";
//                from = "10065";
                headImgUrl = "https://xiaofangyuan.oss-cn-shenzhen.aliyuncs.com/pic/2018/09/15/5dd10d47-0a24-4288-a655-7d738da86c30.png";
            }else if("2".equals(param.getType())){
                nickName = "交易消息";
                from = "66";
//                from = "10066";
                headImgUrl = "https://xiaofangyuan.oss-cn-shenzhen.aliyuncs.com/pic/2018/09/15/cd7397df-feca-4da5-9195-c7bff3b9fb5a.png";
            }else if("3".equals(param.getType())){
                nickName = "社交消息";
                from = "67";
//                from = "10067";
                headImgUrl = "https://xiaofangyuan.oss-cn-shenzhen.aliyuncs.com/pic/2018/09/15/5e70b69c-41e1-4dc7-bac4-5d1c2c5531de.png";
            }else if("4".equals(param.getType())){
                nickName = "新增粉丝";
                from = "68";
//                from = "10068";
                headImgUrl = "https://xiaofangyuan.oss-cn-shenzhen.aliyuncs.com/pic/2018/09/15/d98ddd0a-7b01-4842-8bc2-a070e9a37f29.png";
            }else{
                nickName = "邀请我";
                from = "69";
//                from = "10069";
                headImgUrl = "https://xiaofangyuan.oss-cn-shenzhen.aliyuncs.com/pic/2018/09/15/3d112104-be4d-4622-80d4-fa2e6585a7b7.png";
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
            ext.put("type",param.getType());
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
            /**
             * TODO
             * 1、获取表单提交的formId，存入redis，在下次存入之前都可以生效
             * 2、根据APPID和secret获取access_token，根据失效时间存入redis，每次去redis取，过期就重新获取
             * 3、封装要发送的消息，用户的openId（写死），消息模板id（写死），消息模板跳转页面路径（写死）为json对象
             * 4、根据access_token的token值请求微信，表示我要发送消息
             * 5、腾讯根据此次请求对服务器路径发送验证请求，再验证一次MESSAGE_TOKEN（写死）
             * 6、如果返回正确的值就进行发送消息
             */

//            String message = SendMiniMessage.makeRouteMessage(param.getMiniOpenId(), param.getTemplateId(), param.getPagePath(),param.getMap(),param.ge);
//            AccessToken accessToken = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
//            boolean result = SendMiniMessage.sendTemplateMessage(accessToken.getToken(),message);
            //TODO 判断消息是否成功
//            log.info("判断消息是否成功------:"+result);

//            return toSuccess(result);
            return toSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    public static void main(String[] args) {

//        Date d = new Date();
//        SimpleDateFormat sdf=new SimpleDateFormat(DateUtil.DATE_FORMT);
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); //格式差8小时
//        System.out.println("1："+sdf.format(d));
//        System.out.println("2："+ DateUtil.getFormatDate(d,DateUtil.DATE_FORMT));
    }



    //微信验证开发服务器token
    @GetMapping(value = "/checkSignature")
    @ResponseBody
    public String checkSignature(WeChatMessage message) {
        return weChatMessageService.checkSignature(message);
    }
}
