package com.fangyuanyouyue.user.controller;

import java.io.IOException;
import java.util.List;

import com.fangyuanyouyue.user.dto.UserFansDto;
import com.fangyuanyouyue.user.dto.WaitProcessDto;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.AES;
import com.fangyuanyouyue.base.util.MD5Util;
import com.fangyuanyouyue.user.constant.PhoneCodeEnum;
import com.fangyuanyouyue.user.dto.ShopDto;
import com.fangyuanyouyue.user.dto.UserDto;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.model.WeChatSession;
import com.fangyuanyouyue.user.param.UserParam;
import com.fangyuanyouyue.user.service.SchedualMessageService;
import com.fangyuanyouyue.user.service.SchedualRedisService;
import com.fangyuanyouyue.user.service.UserInfoExtService;
import com.fangyuanyouyue.user.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/user")
@Api(description = "用户系统Controller")
@RefreshScope
public class UserController extends BaseController {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private SchedualRedisService schedualRedisService;//调用redis-service
    @Autowired
    private SchedualMessageService schedualMessageService;//message-service

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
                return toError(ReCode.FAILD.getValue(),"注册平台不能为空！");
            }
            if (StringUtils.isEmpty(param.getPhone())) {
                return toError(ReCode.FAILD.getValue(),"手机号码不能为空！");
            }
            if (StringUtils.isEmpty(param.getLoginPwd())) {
                return toError(ReCode.FAILD.getValue(),"登录密码不能为空！");
            }
            if (StringUtils.isEmpty(param.getNickName())) {
                return toError(ReCode.FAILD.getValue(),"用户昵称不能为空！");
            }
            UserInfo userInfoByName = userInfoService.getUserByNickName(param.getNickName());
            if(userInfoByName != null){
                return toError(ReCode.FAILD.getValue(),"用户昵称已存在！");
            }
            UserInfo userInfo = userInfoService.getUserByPhone(param.getPhone());
            if (userInfo != null) {
                return toError(ReCode.FAILD.getValue(),"手机号码已被注册！");
            }
            //注册
            UserDto userDto = userInfoService.regist(param);
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
            log.info("----》用户登录《----");
            log.info("参数：" + param.toString());
            if (StringUtils.isEmpty(param.getPhone())) {
                return toError(ReCode.FAILD.getValue(),"手机号码不能为空！");
            }
            if (StringUtils.isEmpty(param.getLoginPwd())) {
                return toError(ReCode.FAILD.getValue(),"密码不能为空！");
            }
            if(param.getLoginPlatform() == null){
                return toError(ReCode.FAILD.getValue(),"登录平台不能为空！");
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
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"第三方唯一ID不能为空！");
            }
            if(param.getType() == null){
                return toError(ReCode.FAILD.getValue(),"三方类型不能为空！");
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
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"三方识别号不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            //验证用户
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FAILD.getValue(),"您的账号已被冻结，请联系管理员！");
            }
            //三方绑定
            //三方绑定是为了将微信号与手机号绑定到一个账户，如果用户用手机号注册过，又用微信号登录了第二个账号，将没有绑定功能，而是合并账号，以手机号为主，
            // 如果用户已经存在手机号账号，并登录手机号账户，进行三方绑定，则将微信号绑定到此用户账户上
            UserDto userDto = userInfoService.thirdBind(param.getToken(),param.getUnionId(),param.getType());
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"用户真实姓名不能为空！");
            }
            if(StringUtils.isEmpty(param.getIdentity())){
                return toError(ReCode.FAILD.getValue(),"用户身份照号码不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FAILD.getValue(),"您的账号已被冻结，请联系管理员！");
            }
            //实名认证
            userInfoExtService.certification(param.getToken(),param.getName(),param.getIdentity(),param.getIdentityImgCoverUrl(),param.getIdentityImgBackUrl());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
            @ApiImplicitParam(name = "payPwd", value = "支付密码，md5加密，32位小写字母", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/modify")
    @ResponseBody
    public BaseResp modify(UserParam param) throws IOException {
        try {
            log.info("----》完善资料《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FAILD.getValue(),"您的账号已被冻结，请联系管理员！");
            }
            //手机号不可以重复
            if(StringUtils.isNotEmpty(param.getPhone())){
                UserInfo userByPhone = userInfoService.getUserByPhone(param.getPhone());
                if(userByPhone != null){
                    return toError(ReCode.FAILD.getValue(),"此手机号已被注册！");
                }
            }
            //用户昵称不可以重复
            if(StringUtils.isNotEmpty(param.getNickName())){
                UserInfo userByNickName = userInfoService.getUserByNickName(param.getNickName());
                if(userByNickName != null){
                    return toError(ReCode.FAILD.getValue(),"昵称已存在！");
                }
                if(!param.getNickName().equals(user.getNickName())) {//用户修改了昵称
                    //TODO 昵称筛选
//                    if(param){
//
//                    }
                }
            }
            //TODO 完善资料
            userInfoService.modify(param);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"用户手机不能为空！");
            }
            if(StringUtils.isEmpty(param.getNewPwd())){
                return toError(ReCode.FAILD.getValue(),"新密码不能为空！");
            }
            //找回密码
            userInfoService.resetPwd(param.getPhone(),param.getNewPwd());
            return toSuccess("找回密码成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FAILD.getValue(),"您的账号已被冻结，请联系管理员！");
            }
            if(StringUtils.isEmpty(user.getPhone())){
                return toError(ReCode.FAILD.getValue(),"第三方用户不可修改密码！");
            }
            //判断旧密码是否正确
            if(!MD5Util.verify(MD5Util.MD5(param.getLoginPwd()),user.getLoginPwd())){
                return toError(ReCode.FAILD.getValue(),"旧密码不正确！");
            }
            //修改密码
            userInfoService.updatePwd(param.getToken(),param.getNewPwd());
            return toSuccess("修改密码成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"新的手机号码不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user == null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FAILD.getValue(),"您的账号已被冻结，请联系管理员！");
            }
            if(user.getPhone() != null && !user.getPhone().equals("")){
                if(user.getPhone().equals(param.getPhone())){
                    return toError(ReCode.FAILD.getValue(),"不能与旧手机号相同！");
                }
            }
            UserInfo oldUser = userInfoService.getUserByPhone(param.getPhone());
            if(oldUser != null){
                return toError(ReCode.FAILD.getValue(),"该手机已被其他帐号绑定，请不要重复绑定！");
            }
            //修改绑定手机
            UserDto userDto = userInfoService.updatePhone(param.getToken(),param.getPhone());
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "合并账号", notes = "(UserDto)合并账号",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/accountMerge")
    @ResponseBody
    public BaseResp accountMerge(UserParam param) throws IOException{
        try {
            log.info("----》合并账号《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getPhone())){
                return toError(ReCode.FAILD.getValue(),"手机号码不能为空！");
            }
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user == null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FAILD.getValue(),"您的账号已被冻结，请联系管理员！");
            }
            //TODO 合并账号,一定是从三方账号发起请求
            UserDto userDto = userInfoService.accountMerge(param.getToken(),param.getPhone());
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                    return toError(ReCode.FAILD.getValue(),"页面授权失败！");
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
                            return toError(ReCode.FAILD.getValue(),"解密异常!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return toError(ReCode.FAILD.getValue(),"解密异常!检查解密数据！");
                    }
                }else{
                    //获取到unionId，注册/登录
                    param.setUnionId(unionid);
                    UserDto userDto = userInfoService.miniLogin(param,openid,session_key);
                    //最后要返回一个自定义的登录态,用来做后续数据传输的验证
                    return toSuccess(userDto);
                }
            }else{
                return toError(ReCode.FAILD.getValue(),"页面授权失败！");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"手机号码不能为空！");
            }
            if(param.getType() == null){
                return toError(ReCode.FAILD.getValue(),"类型不能为空！");
            }
            //验证用户
            //根据手机号获取用户，如果存在，则说明为旧手机号,调用user-service
            UserInfo userInfo=userInfoService.getUserByPhone(param.getPhone());
            if(PhoneCodeEnum.TYPE_REGIST.getCode() == param.getType()){//使用手机号注册新用户
                if(userInfo != null){
                    return toError(ReCode.FAILD.getValue(),"此手机号已被注册！");
                }
            }else if(PhoneCodeEnum.TYPE_FINDPWD.getCode() == param.getType()){//为1 找回密码
                if(userInfo == null){
                    return toError(ReCode.FAILD.getValue(),"用户不存在，请注册！");
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
                if(userInfo != null){
                    return toError(ReCode.FAILD.getValue(),"此手机号已被注册！");
                }
            }else if(PhoneCodeEnum.TYPE_AUTH.getCode() == param.getType()){//为5认证店铺
				/*if(count == 0){
					return toError(ReCode.FAILD.getValue(),"此手机号尚未注册！");
				}*/
            }else if(PhoneCodeEnum.ADDFORUM.getCode() == param.getType()){//为6申请专栏

            }else{

            }
            //调用短信系统发送短信
            JSONObject jsonObject = JSONObject.parseObject(schedualMessageService.sendCode(param.getPhone(),param.getType()));
            String code = jsonObject.getString("data");
            log.info("code---:"+code);

            boolean result = schedualRedisService.set(param.getPhone(), code, 60l);
            log.info("缓存结果："+result);
            
            //redisTemplate.opsForValue().set(param.getPhone(),code);
            //redisTemplate.expire(param.getPhone(),60,TimeUnit.SECONDS);
            return toSuccess("发送验证码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"手机号码不能为空！");
            }
            if(StringUtils.isEmpty(param.getCode())){
                return toError(ReCode.FAILD.getValue(),"验证码不能为空！");
            }
            //TODO 从缓存获取
            String code = String.valueOf(schedualRedisService.get(param.getPhone()));
            log.info("验证码:1."+code+" 2."+param.getCode());
            if(StringUtils.isEmpty(code) || !code.equals(param.getCode())){
                return toError(ReCode.FAILD.getValue(),"验证码错误！");
            }

            return toSuccess("验证验证码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError(ReCode.FAILD.getValue(),"每页个数错误！");
            }

            List<ShopDto> shopDtos = userInfoService.shopList(param.getNickName(),param.getType(), param.getStart(), param.getLimit(),param.getAuthType());
            return toSuccess(shopDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"用户ID不能为空！");
            }
            if(StringUtils.isNotEmpty(param.getToken())) {//验证用户
                UserInfo user=userInfoService.getUserByToken(param.getToken());
                if(user==null){
                    return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
                }
                if(user.getStatus() == 2){
                    return toError(ReCode.FAILD.getValue(),"您的账号已被冻结，请联系管理员！");
                }
            }
            UserDto userDto = userInfoService.userInfo(param.getToken(),param.getUserId());
            return toSuccess(userDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FAILD.getValue(),"您的账号已被冻结，请联系管理员！");
            }
            if(user.getId() == param.getToUserId()){
                return toError(ReCode.FAILD.getValue(),"不能关注自己");
            }
            if(param.getToUserId() == null){
                return toError(ReCode.FAILD.getValue(),"被关注人不能为空！");
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
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "我的关注/我的粉丝", notes = "（UserFansDto）我的关注/我的粉丝")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1我的关注 2我的粉丝", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "分页start", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "分页limit", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/myFansOrFollows")
    @ResponseBody
    public BaseResp myFansOrFollows(UserParam param) throws IOException {
        try {
            log.info("----》我的关注/我的粉丝《----");
            log.info("参数："+param.toString());
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            if(user.getStatus() == 2){
                return toError(ReCode.FAILD.getValue(),"您的账号已被冻结，请联系管理员！");
            }
            if(param.getStart() == null || param.getStart() < 0){
                return toError(ReCode.FAILD.getValue(),"起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError(ReCode.FAILD.getValue(),"每页个数错误！");
            }
            if(param.getType() == null){
                return toError(ReCode.FAILD.getValue(),"类型不能为空！");
            }
            //我的关注/我的粉丝
            List<UserFansDto> userFansDtos = userInfoService.myFansOrFollows(user.getId(), param.getType(),param.getStart(), param.getLimit());
            return toSuccess(userFansDtos);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
                return toError(ReCode.FAILD.getValue(),"用户ID不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            //获取待处理信息
            WaitProcessDto waitProcessDto = userInfoService.myWaitProcess(user.getId());
            return toSuccess(waitProcessDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "申请官方认证", notes = "（void）申请官方认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/authType")
    @ResponseBody
    public BaseResp authType(UserParam param) throws IOException {
        try {
            log.info("----》申请官方认证《----");
            log.info("参数："+param.toString());
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户ID不能为空！");
            }
            UserInfo user=userInfoService.getUserByToken(param.getToken());
            if(user==null){
                return toError(ReCode.FAILD.getValue(),"登录超时，请重新登录！");
            }
            //申请官方认证
            userInfoExtService.authType(user.getId());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
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
