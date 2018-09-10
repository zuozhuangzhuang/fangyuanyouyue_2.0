package com.fangyuanyouyue.user.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fangyuanyouyue.base.model.WxPayResult;
import com.fangyuanyouyue.base.util.WechatUtil.WXPayUtil;
import com.fangyuanyouyue.base.util.alipay.util.AlipayNotify;
import com.fangyuanyouyue.user.dto.*;
import com.fangyuanyouyue.user.model.UserThirdParty;
import com.fangyuanyouyue.user.service.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.AES;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.user.constant.PhoneCodeEnum;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.model.WeChatSession;
import com.fangyuanyouyue.user.param.UserParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/user")
@Api(description = "用户系统Controller")
@RefreshScope
public class UserController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    protected Logger wechatLog = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private SchedualRedisService schedualRedisService;//调用redis-service
    @Autowired
    private SchedualMessageService schedualMessageService;//message-service
    @Autowired
    private UserThirdService userThirdService;



    @ApiOperation(value = "注册", notes = "(UserDto)注册",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号",required = true, dataType = "String", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "loginPwd", value = "登录密码,MD5小写",required = true, dataType = "String", paramType = "query",example = "123456"),
            @ApiImplicitParam(name = "nickName", value = "昵称",required = true, dataType = "String", paramType = "query",example = "测试用户"),
            @ApiImplicitParam(name = "headImgUrl", value = "头像图片路径", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bgImgUrl", value = "背景图片路径", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "性别，1男 2女 0不确定", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "regPlatform", value = "注册平台 1安卓 2iOS 3小程序", required = true, dataType = "int", paramType = "query",example = "1")
    })
    @PostMapping(value = "/regist")
    @ResponseBody
    public BaseResp regist(UserParam param) throws IOException {
        try {
            log.info("----》注册《----");
            log.info("参数：" + param.toString());
            if(param.getRegPlatform() == null){
                return toError("注册平台不能为空！");
            }
            if (StringUtils.isEmpty(param.getPhone())) {
                return toError("手机号码不能为空！");
            }
            if (StringUtils.isEmpty(param.getLoginPwd())) {
                return toError("登录密码不能为空！");
            }
            if (StringUtils.isEmpty(param.getNickName())) {
                return toError("用户昵称不能为空！");
            }
            UserInfo userInfoByName = userInfoService.getUserByNickName(param.getNickName());
            if(userInfoByName != null){
                return toError("用户昵称已存在！");
            }
            UserInfo userInfo = userInfoService.getUserByPhone(param.getPhone());
            if (userInfo != null) {
                return toError("手机号码已被注册！");
            }
            //注册
            UserDto userDto = userInfoService.regist(param);
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "用户登录", notes = "(UserDto)用户登录",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "loginPwd", value = "登录密码,MD5小写", required = true, dataType = "String", paramType = "query",example = "123456"),
            @ApiImplicitParam(name = "loginPlatform", value = "登录平台 1安卓 2iOS 3小程序", required = true, dataType = "int", paramType = "query",example = "1")
    })
    @PostMapping(value = "/login")
    @ResponseBody
    public BaseResp login(UserParam param) throws IOException {
        try {
            log.info("\n" +
                    "                                                __----~~~~~~~~~~~------___\n" +
                    "                                          .  .   ~~//====......          __--~ ~~\n" +
                    "                          -.            \\_|//     |||\\\\  ~~~~~~::::... /~\n" +
                    "                       ___-==_       _-~o~  \\/    |||  \\\\            _/~~-\n" +
                    "               __---~~~.==~||\\=_    -_--~/_-~|-   |\\\\   \\\\        _/~\n" +
                    "           _-~~     .=~    |  \\\\-_    '-~7  /-   /  ||    \\      /\n" +
                    "         .~       .~       |   \\\\ -_    /  /-   /   ||      \\   /\n" +
                    "        /  ____  /         |     \\\\ ~-_/  /|- _/   .||       \\ /\n" +
                    "        |~~    ~~|--~~~~--_ \\     ~==-/   | \\~--===~~        .\\\n" +
                    "                 '         ~-|      /|    |-~\\~~       __--~~\n" +
                    "                             |-~~-_/ |    |   ~\\_   _-~            /\\\n" +
                    "                                  /  \\     \\__   \\/~                \\__\n" +
                    "                              _--~ _/ | .-~~____--~-/                  ~~==.\n" +
                    "                             ((->/~   '.|||' -_|    ~~-/ ,              . _||\n" +
                    "                                        -_     ~\\      ~~---l__i__i__i--~~_/\n" +
                    "                                        _-~-__   ~)  \\--______________--~~\n" +
                    "                                      //.-~~~-~_--~- |-------~~~~~~~~\n" +
                    "                                             //.-~~~--\\\n");
            log.info("----》用户登录《----");
            log.info("参数：" + param.toString());
            if (StringUtils.isEmpty(param.getPhone())) {
                return toError("手机号码不能为空！");
            }
            if (StringUtils.isEmpty(param.getLoginPwd())) {
                return toError("密码不能为空！");
            }
            if(param.getLoginPlatform() == null){
                return toError("登录平台不能为空！");
            }
            //MD5加密
//            param.setLoginPwd(MD5Util.generate(MD5Util.MD5(param.getLoginPwd())));
            //用户登录
            UserDto userDto = userInfoService.login(param.getPhone(),param.getLoginPwd(),param.getLoginPlatform());
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "APP三方注册/登录", notes = "(UserDto)APP三方注册/登录",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thirdNickName", value = "第三方账号昵称", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "thirdHeadImgUrl", value = "第三方账号头像地址",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "性别，1男 2女 0不确定", required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "loginPlatform", value = "登录平台 1安卓 2iOS 3小程序",required = true,  dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "unionId", value = "第三方唯一ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1微信 2QQ 3微博", required = true, dataType = "int", paramType = "query"),
    })
    @PostMapping(value = "/thirdLogin")
    @ResponseBody
    public BaseResp thirdLogin(UserParam param) throws IOException {
        try {
            log.info("----》APP三方注册/登录《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getUnionId())){
                return toError("第三方唯一ID不能为空！");
            }
            if(param.getType() == null){
                return toError("三方类型不能为空！");
            }
            //APP三方注册/登录
            param.setRegType(1);//注册来源 1app 2微信小程序
            UserDto userDto = userInfoService.thirdLogin(param);
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "三方绑定", notes = "(UserDto)三方绑定",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "unionId", value = "第三方唯一ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1微信 2QQ 3微博", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/thirdBind")
    @ResponseBody
    public BaseResp thirdBind(UserParam param) throws IOException {
        try {
            log.info("----》三方绑定《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getUnionId())){
                return toError("三方识别号不能为空！");
            }
            if(param.getType() == null){
                return toError("三方类型不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            //验证用户
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            //三方绑定
            //三方绑定是为了将微信号与手机号绑定到一个账户，如果用户用手机号注册过，又用微信号登录了第二个账号，将没有绑定功能，而是合并账号，以手机号为主，
            // 如果用户已经存在手机号账号，并登录手机号账户，进行三方绑定，则将微信号绑定到此用户账户上
            UserDto userDto = userInfoService.thirdBind(param.getToken(),param.getUnionId(),param.getType());
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            //如果可以合并账号，就返回 code = 2
            if(e.getCode() != null){
                return toError(e.getCode(),e.getMessage());
            }
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "实名认证", notes = "(void)实名认证",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "真实姓名", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "identity", value = "身份证号", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "identityImgCoverUrl", value = "身份证封面图路径",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "identityImgBackUrl", value = "身份证背面路径",dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/certification")
    @ResponseBody
    public BaseResp certification(UserParam param) throws IOException {
        try {
            log.info("----》实名认证《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getName())){
                return toError("用户真实姓名不能为空！");
            }
            if(StringUtils.isEmpty(param.getIdentity())){
                return toError("用户身份照号码不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            //实名认证
            userInfoExtService.certification(param.getToken(),param.getName(),param.getIdentity(),param.getIdentityImgCoverUrl(),param.getIdentityImgBackUrl());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "完善资料", notes = "(void)完善资料",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号码",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "电子邮件",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userAddress", value = "用户所在地",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "headImgUrl", value = "头像图片路径",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bgImgUrl", value = "背景图片路径",dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "性别，1男 2女 0不确定", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "个性签名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "contact", value = "联系电话", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "identity", value = "身份证号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "真实姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码，md5加密，32位小写字母", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginPwd", value = "登录密码，md5加密，32位小写字母", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/modify")
    @ResponseBody
    public BaseResp modify(UserParam param) throws IOException {
        try {
            log.info("----》完善资料《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            //手机号不可以重复
            if(StringUtils.isNotEmpty(param.getPhone())){
                //三方用户未绑定手机，手机用户未绑定次type三方时可以选择合并用户，需要验证手机号
                UserInfo userByPhone = userInfoService.getUserByPhone(param.getPhone());
                if(userByPhone != null){
                    MergeDto mergeDto = userThirdService.judgeMerge(param.getToken(), null, param.getPhone(),null);
                    if(mergeDto == null){
                        //可以合并账号
                        return toError(2,"此手机号已被注册，是否合并账号！");
                    }
                    return toError("此手机号已被注册！");
                }
            }
            //用户昵称不可以重复
            if(StringUtils.isNotEmpty(param.getNickName())){
                UserInfo userByNickName = userInfoService.getUserByNickName(param.getNickName());
                if(userByNickName != null){
                    return toError("昵称已存在！");
                }
                if(!param.getNickName().equals(user.getNickName())) {//用户修改了昵称
                    //TODO 昵称筛选
//                    if(param){
//
//                    }
                }
            }
            //完善资料
            userInfoService.modify(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

@ApiOperation(value = "获取合并账号用户信息", notes = "(MergeDto)获取合并账号用户信息:目前只支持三方账户绑定手机号",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "unionId", value = "第三方唯一ID(暂不支持)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1微信 2QQ 3微博(暂不支持)", required = false, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/getMergeUser")
    @ResponseBody
    public BaseResp getMergeUser(UserParam param) throws IOException {
        try {
            log.info("----》获取合并账号用户信息《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            if(StringUtils.isEmpty(param.getPhone())){
                return toError("手机号码不能为空！");
            }

            MergeDto mergeDto = userThirdService.judgeMerge(param.getToken(), param.getUnionId(), param.getPhone(),param.getType());
            return toSuccess(mergeDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "找回密码", notes = "(void)找回密码",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "用户手机", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "newPwd", value = "新密码密码，md5加密，32位小写字母",required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/resetPwd")
    @ResponseBody
    public BaseResp resetPwd(UserParam param) throws IOException {
        try {
            log.info("----》找回密码《----");
            log.info("参数："+param.getPhone()+"---"+param.getNewPwd());
            if(param.getPhone() == null){
                return toError("用户手机不能为空！");
            }
            if(StringUtils.isEmpty(param.getNewPwd())){
                return toError("新密码不能为空！");
            }
            //找回密码
            userInfoService.resetPwd(param.getPhone(),param.getNewPwd());
            return toSuccess("找回密码成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "修改密码", notes = "(void)修改密码",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginPwd", value = "登录密码，md5加密，32位小写字母", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "newPwd", value = "新密码密码，md5加密，32位小写字母",required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updatePwd")
    @ResponseBody
    public BaseResp updatePwd(UserParam param) throws IOException {
        try {
            log.info("----》修改密码《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            if(StringUtils.isEmpty(user.getPhone())){
                return toError("第三方用户不可修改密码！");
            }
            //判断旧密码是否正确
            if(!MD5Util.verify(MD5Util.MD5(param.getLoginPwd()),user.getLoginPwd())){
                return toError("旧密码不正确！");
            }
            //修改密码
            userInfoService.updatePwd(param.getToken(),param.getNewPwd());
            return toSuccess("修改密码成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "修改绑定手机", notes = "(UserDto)修改绑定手机",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updatePhone")
    @ResponseBody
    public BaseResp updatePhone(UserParam param) throws IOException {
        try {
            log.info("----》修改绑定手机《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getPhone())){
                return toError("新的手机号码不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user == null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            if(user.getPhone() != null && !user.getPhone().equals("")){
                if(user.getPhone().equals(param.getPhone())){
                    return toError("不能与旧手机号相同！");
                }
            }
            UserInfo oldUser = userInfoService.getUserByPhone(param.getPhone());
            if(oldUser != null){
                return toError("该手机已被其他帐号绑定，请不要重复绑定！");
            }
            //修改绑定手机
            UserDto userDto = userInfoService.updatePhone(param.getToken(),param.getPhone());
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "合并账号", notes = "(String)合并账号",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号(只支持三方账户绑定手机号)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "unionId", value = "第三方唯一ID(暂不支持)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1微信 2QQ 3微博(暂不支持)", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "loginPwd", value = "登录密码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/accountMerge")
    @ResponseBody
    public BaseResp accountMerge(UserParam param) throws IOException{
        try {
            log.info("----》合并账号《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user == null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            if (StringUtils.isEmpty(param.getLoginPwd())) {
                return toError("密码不能为空！");
            }
            //合并账号,1、手机号绑定三方账号 （暂不支持）2、三方账号绑定手机号
            userThirdService.accountMerge(param.getToken(),param.getPhone(),param.getUnionId(),param.getType(),param.getLoginPwd());
            return toSuccess("账号已合并，请重新登录！");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "小程序登录", notes = "(UserDto)小程序登录",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "code值", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "thirdNickName", value = "第三方账号昵称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "thirdHeadImgUrl", value = "第三方账号头像地址", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "性别，1男 2女 0不确定", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1微信 2QQ 3微博",required = true,  dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "iv", value = "加密算法的初始向量", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/miniLogin")
    @ResponseBody
    public BaseResp miniLogin(UserParam param) throws IOException{
        try{
            log.info("----》小程序登录《----");
            log.info("参数："+param.toString());
            //微信的接口
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+WeChatSession.APPID+
                    "&secret="+WeChatSession.SECRET+"&js_code="+ param.getCode() +"&grant_type=authorization_code";
            RestTemplate restTemplate = new RestTemplate();
            //进行网络请求,访问url接口
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            //根据返回值进行后续操作
            if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK){
                String sessionData = responseEntity.getBody();
                //解析从微信服务器获得的openid和session_key;
                WeChatSession weChatSession = JSONObject.parseObject(sessionData,WeChatSession.class);
                if(weChatSession == null){
                    return toError("页面授权失败！");
                }
                //微信用户在此小程序的识别号
                String openid = weChatSession.getOpenid();
                //获取会话秘钥
                String session_key = weChatSession.getSession_key();
                //正常情况只返回openID和session_key，注册过的用户会额外返回unionID
                //微信用户的固定唯一识别号
                String unionid = weChatSession.getUnionid();
                if(StringUtils.isEmpty(unionid)){
                    //获取不到unionId，根据算法解密得到unionID
                    // 被加密的数据
                    byte[] dataByte = Base64.decodeBase64(weChatSession.getEncryptedData());
                    // 加密秘钥
                    byte[] aeskey = Base64.decodeBase64(session_key);
                    // 偏移量
                    byte[] ivByte = Base64.decodeBase64(weChatSession.getIv());
                    String newuserInfo;
                    try {
                        //AES解密
                        AES aes = new AES();
                        byte[] resultByte = aes.decrypt(dataByte, aeskey, ivByte);
                        if (null != resultByte && resultByte.length > 0) {
                            newuserInfo = new String(resultByte, "UTF-8");
                            log.info("解密完毕,解密结果为newuserInfo:"+ newuserInfo);
                            JSONObject jsonObject = JSONObject.parseObject(newuserInfo);
                            unionid = jsonObject.getString("unionid");
                            param.setUnionId(unionid);
                            UserDto userDto = userInfoService.miniLogin(param,openid,session_key);
                            return toSuccess(userDto);
                        }else{
                            return toError("解密异常!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return toError("解密异常!检查解密数据！");
                    }
                }else{
                    //获取到unionId，注册/登录
                    param.setUnionId(unionid);
                    UserDto userDto = userInfoService.miniLogin(param,openid,session_key);
                    //最后要返回一个自定义的登录态,用来做后续数据传输的验证
                    return toSuccess(userDto);
                }
            }else{
                return toError("页面授权失败！");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "发送验证码", notes = "(void)发送验证码",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "用户手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "验证码类型 0注册 1表找回密码 2 设置/修改支付密码 3验证旧手机，4绑定新手机 5店铺认证 6申请专栏", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "unionId", value = "三方唯一识别号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "thirdType", value = "类型 1微信 2QQ 3微博", dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/sendCode")
    @ResponseBody
    public BaseResp sendCode(UserParam param) throws IOException {
        try {
            log.info("----》发送验证码《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getPhone())){
                return toError("手机号码不能为空！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            //验证用户
            //根据手机号获取用户，如果存在，则说明为旧手机号,调用user-service
            UserInfo userInfo=userInfoService.getUserByPhone(param.getPhone());
            if(PhoneCodeEnum.TYPE_REGIST.getCode() == param.getType()){//使用手机号注册新用户
                if(userInfo != null){
                    return toError("此手机号已被注册！");
                }
            }else if(PhoneCodeEnum.TYPE_FINDPWD.getCode() == param.getType()){//为1 找回密码
                if(userInfo == null){
                    return toError("用户不存在，请注册！");
                }
            }else if(PhoneCodeEnum.TYPE_SET_PAY_PWD.getCode() == param.getType()){//2 设置支付密码

            }else if(PhoneCodeEnum.TYPE_OLD_PHONE.getCode() == param.getType()){//为3验证旧手机，给旧手机发验证码去验证
//                if(userInfo != null){//已存在此手机号
//                    //验证此手机是否存在其他识别号
//                    if(StringUtils.isNotEmpty(param.getUnionId())){//根据传入参数判断是qq还是微信绑定
//
//                    }
//                }
            }else if(PhoneCodeEnum.TYPE_NEW_PHONE.getCode() == param.getType()){//为4绑定新手机
            }else if(PhoneCodeEnum.TYPE_AUTH.getCode() == param.getType()){//为5认证店铺
				/*if(count == 0){
					return toError("此手机号尚未注册！");
				}*/
            }else if(PhoneCodeEnum.ADDFORUM.getCode() == param.getType()){//为6申请专栏

            }else{

            }
            //调用短信系统发送短信
//            JSONObject jsonObject = JSONObject.parseObject(schedualMessageService.sendCode(param.getPhone(),param.getType()));
//            String code = jsonObject.getString("data");
            //TODO 开发固定1234
            String code = "1234";
            log.info("code---:"+code);

            boolean result = schedualRedisService.set(param.getPhone(), code, 600l);
            log.info("缓存结果："+result);
            
            //redisTemplate.opsForValue().set(param.getPhone(),code);
            //redisTemplate.expire(param.getPhone(),60,TimeUnit.SECONDS);
            return toSuccess("发送验证码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "验证验证码", notes = "(void)验证验证码",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "用户手机号", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/verifyCode")
    @ResponseBody
    public BaseResp verifyCode(UserParam param) throws IOException {

        try {
            log.info("----》验证验证码《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getPhone())){
                return toError("手机号码不能为空！");
            }
            if(StringUtils.isEmpty(param.getCode())){
                return toError("验证码不能为空！");
            }
            //从缓存获取
            // FIXME: 2018/8/17 验证码以0开头时验证异常 Leading zeroes not allowed
            if(schedualRedisService.get(param.getPhone()) == null){
                return toError("验证码已失效，请重新获取验证码！");
            }
            String code = schedualRedisService.get(param.getPhone()).toString();
            log.info("验证码:1."+code+" 2."+param.getCode());
            if(StringUtils.isEmpty(code) || !code.equals(param.getCode())){
                return toError("验证码错误！");
            }

            return toSuccess("验证验证码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "获取个人店铺列表", notes = "(ShopDto)获取个人店铺列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickName", value = "用户昵称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "authType", value = "获取认证店铺列表 认证状态 1已认证", dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/shopList")
    @ResponseBody
    public BaseResp shopList(UserParam param) throws IOException {
        try {
            log.info("----》获取个人店铺列表《----");
            log.info("参数："+param.toString());
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }

            List<ShopDto> shopDtos = userInfoService.shopList(param.getNickName(),param.getType(), param.getStart(), param.getLimit(),param.getAuthType());
            return toSuccess(shopDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "获取用户信息", notes = "(UserDto)获取用户信息",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "卖家ID", required = true,dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/userInfo")
    @ResponseBody
    public BaseResp userInfo(UserParam param) throws IOException {
        try {
            log.info("----》获取用户信息《----");
            log.info("参数："+param.toString());
            if(param.getUserId() == null){
                return toError("用户ID不能为空！");
            }
            UserDto userDto = userInfoService.userInfo(param.getToken(),param.getUserId());
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "添加/取消关注", notes = "(void)添加/取消关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "toUserId", value = "被关注用户id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "0关注 1取消关注", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/fansFollow")
    @ResponseBody
    public BaseResp fansFollow(UserParam param) throws IOException {
        try {
            log.info("----》添加关注/取消关注《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            if(user.getId() == param.getToUserId()){
                return toError("不能关注自己");
            }
            if(param.getToUserId() == null){
                return toError("被关注人不能为空！");
            }
            
            //添加/取消关注
            userInfoService.fansFollow(user.getId(), param.getToUserId(),param.getType());
            if(param.getType() == 0){
                return toSuccess("添加关注成功！");
            }else{
                return toSuccess("取消关注成功！");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "我的关注/我的粉丝", notes = "（UserFansDto）我的关注/我的粉丝")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1我的关注 2我的粉丝", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索条件",required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/myFansOrFollows")
    @ResponseBody
    public BaseResp myFansOrFollows(UserParam param) throws IOException {
        try {
            log.info("----》我的关注/我的粉丝《----");
            log.info("参数："+param.toString());
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            //我的关注/我的粉丝
            List<UserFansDto> userFansDtos = userInfoService.myFansOrFollows(user.getId(), param.getType(),param.getStart(), param.getLimit(),param.getSearch());
            return toSuccess(userFansDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "获取待处理信息", notes = "（WaitProcessDto）获取待处理信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/myWaitProcess")
    @ResponseBody
    public BaseResp myWaitProcess(UserParam param) throws IOException {
        try {
            log.info("----》获取待处理信息《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            //获取待处理信息
            WaitProcessDto waitProcessDto = userInfoService.myWaitProcess(user.getId());
            return toSuccess(waitProcessDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "申请官方认证", notes = "（void）申请官方认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "支付方式 1微信 2支付宝 3余额", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/authType")
    @ResponseBody
    public BaseResp authType(UserParam param) throws IOException {
        try {
            log.info("----》申请官方认证《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError("登录超时，请重新登录！");
            }
            if(param.getPayType() == null){
                return toError("支付方式不能为空！");
            }
            //申请官方认证
            Object info = userInfoExtService.authType(user.getId(), param.getPayType(), param.getPayPwd());
            return toSuccess(info);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "根据用户名获取用户列表", notes = "（ShopDto）根据用户名获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "查询内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/getUserByName")
    @ResponseBody
    public BaseResp getUserByName(UserParam param) throws IOException {
        try {
            log.info("----》根据用户名获取用户列表《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getSearch())){
                return toError("查询内容不能为空！");
            }
            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            //根据用户名获取用户列表
            List<ShopDto> userByName = userInfoService.getUserByName(param.getSearch(),param.getStart(),param.getLimit());
            return toSuccess(userByName);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "申请官方认证回调接口", notes = "申请官方认证", response = BaseResp.class)
    @PostMapping(value = "/notify/wechat")
    @ResponseBody
    public String notify(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String resXml = "";

        try{
            //把如下代码贴到的你的处理回调的servlet 或者.do 中即可明白回调操作
            wechatLog.info("微信支付小方圆回调数据开始");


            //示例报文
            //		String xml = "<xml><appid><![CDATA[wxb4dc385f953b356e]]></appid><bank_type><![CDATA[CCB_CREDIT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1228442802]]></mch_id><nonce_str><![CDATA[1002477130]]></nonce_str><openid><![CDATA[o-HREuJzRr3moMvv990VdfnQ8x4k]]></openid><out_trade_no><![CDATA[1000000000051249]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[1269E03E43F2B8C388A414EDAE185CEE]]></sign><time_end><![CDATA[20150324100405]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1009530574201503240036299496]]></transaction_id></xml>";
            String inputLine;
            String notityXml = "";
            try {
                while ((inputLine = request.getReader().readLine()) != null) {
                    notityXml += inputLine;
                }
                request.getReader().close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            wechatLog.info("接收到的报文：" + notityXml);


//			Map m = parseXmlToList2(notityXml);
            Map m = WXPayUtil.xmlToMap(notityXml);
            WxPayResult wpr = new WxPayResult();
            if(m != null){
                wpr.setAppid(m.get("appid").toString());
                wpr.setBankType(m.get("bank_type").toString());
                wpr.setCashFee(m.get("cash_fee").toString());
                //wpr.setFeeType(m.get("fee_type").toString());
                //wpr.setIsSubscribe(m.get("is_subscribe").toString());
                wpr.setMchId(m.get("mch_id").toString());
                wpr.setNonceStr(m.get("nonce_str").toString());
                wpr.setOpenid(m.get("openid").toString());
                wpr.setOutTradeNo(m.get("out_trade_no").toString());
                wpr.setResultCode(m.get("result_code").toString());
                wpr.setReturnCode(m.get("return_code")==null?"":m.get("return_code").toString());
                wpr.setSign(m.get("sign").toString());
                wpr.setTimeEnd(m.get("time_end").toString());
                wpr.setTotalFee(m.get("total_fee").toString());
                wpr.setTradeType(m.get("trade_type").toString());
                wpr.setTransactionId(m.get("transaction_id").toString());
            }
            wechatLog.info("返回信息："+wpr.toString());
            if("SUCCESS".equals(wpr.getResultCode())){
                //支付成功
                boolean result = userInfoExtService.updateOrder(wpr.getOutTradeNo(), wpr.getTransactionId(),1);

                wechatLog.info("支付成功！");
                if(result){
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

                    wechatLog.info("处理成功！");
                }else{
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[FAILD]]></return_msg>" + "</xml> ";

                    wechatLog.info("处理失败！");
                }


            }else{
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                wechatLog.info("支付失败！");
            }

            wechatLog.info("微信支付回调结束");


        } catch (Exception e) {
            e.printStackTrace();
            wechatLog.error("微信通知后台处理系统出错", e);
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return resXml;
    }

    @ApiOperation(value = "申请官方认证支付宝回调接口", notes = "官方认证支付宝回调", response = BaseResp.class,hidden = true)
    @RequestMapping(value = "/notify/alipay", method = RequestMethod.POST)
    @ResponseBody
    public String orderNotify(HttpServletRequest request) throws IOException {

        log.info("-----------支付宝后台通知-----------");
        //HttpServletRequest request = getRequest();
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        String response = "";
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);

        }
        for (String key : params.keySet()) {
            response += key + "=" + params.get(key) + ",";
        }
        if (response.equals("")) {
            log.warn("无数据返回");
            return "";
        }
        log.warn("支付宝响应报文[订单号:" + params.get("out_trade_no") + "]：" + response);
        String ret = "";
        try {
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            // 商户订单号
            String out_trade_no = new String(request.getParameter(
                    "out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            log.info("商户订单号：" + out_trade_no);
            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no")
                    .getBytes("ISO-8859-1"), "UTF-8");
            log.info("支付宝交易号：" + trade_no);
            // 交易状态
            String trade_status = new String(request.getParameter(
                    "trade_status").getBytes("ISO-8859-1"), "UTF-8");

            log.info("交易状态：" + trade_status);
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            log.info("支付响应报文开始验证");
            if (AlipayNotify.verify(params)) {// 验证成功

                log.info("支付宝支付验证成功[订单号:" + trade_no + "]");
                // ////////////////////////////////////////////////////////////////////////////////////////
                // 请在这里加上商户的业务逻辑程序代码

                // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

                if (trade_status.equals("TRADE_FINISHED")) {

                    log.info("支付宝支付完成！TRADE_FINISHED");
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序
//                    boolean result = orderService.saveNotify(out_trade_no, trade_no,Type.PAYTYPE_ALIPAY.getValue());
                    boolean result = userInfoExtService.updateOrder(out_trade_no,trade_no,2);
                    if(result){
                        ret = "success"; // 请不要修改或删除
                    }else{
                        ret = "fail";
                    }
                    // 注意：
                    // 该种交易状态只在两种情况下出现
                    // 1、开通了普通即时到账，买家付款成功后。
                    // 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
                } else if (trade_status.equals("TRADE_SUCCESS")) {

                    log.info("支付宝支付完成！TRADE_SUCCESS");

                    // service.doUpdate(out_trade_no);
                    boolean result = userInfoExtService.updateOrder(out_trade_no, trade_no,2);
                    if(result){
                        ret = "success"; // 请不要修改或删除
                    }else{
                        ret = "fail";
                    }
                    // 判断该笔订单是否在商户网站中已经做过处理
                    // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    // 如果有做过处理，不执行商户的业务程序

                    // 注意：
                    // 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
                }else{
                    ret = "fail";
                }

                // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——


                // ////////////////////////////////////////////////////////////////////////////////////////
            } else {// 验证失败
                log.error("支付宝支付验证失败！");
                ret = "fail";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付宝通知后台处理系统出错", e);
            ret = "fail";
        }

        return ret;

    }


    public static void main(String[] args) {
        //微信获取的code
        String code = "";
        //包括敏感数据在内的完整用户信息的加密数据
        String encryptedData = "";
        //加密算法的初始向量
        String iv = "";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+WeChatSession.APPID+
                "&secret="+WeChatSession.SECRET+"&js_code="+ code +"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        //进行网络请求,访问url接口
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        System.out.println(responseEntity);
        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
            String sessionData = responseEntity.getBody();
            WeChatSession weChatSession = JSONObject.parseObject(sessionData,WeChatSession.class);
            //微信用户在此小程序的识别号
            String openid = weChatSession.getOpenid();
            //获取会话秘钥
            String session_key = weChatSession.getSession_key();
            System.out.println("openid:"+openid);
            System.out.println("session_key:"+session_key);
            // 被加密的数据
            byte[] dataByte = Base64.decodeBase64(encryptedData);
            // 加密秘钥
            byte[] aeskey = Base64.decodeBase64(session_key);
            // 偏移量
            byte[] ivByte = Base64.decodeBase64(iv);
            String newuserInfo = "";
            try {
                AES aes = new AES();
                byte[] resultByte = aes.decrypt(dataByte, aeskey, ivByte);
                if (null != resultByte && resultByte.length > 0) {
                    newuserInfo = new String(resultByte, "UTF-8");
                    System.out.println("解密完毕,解密结果为newuserInfo:"+ newuserInfo);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
