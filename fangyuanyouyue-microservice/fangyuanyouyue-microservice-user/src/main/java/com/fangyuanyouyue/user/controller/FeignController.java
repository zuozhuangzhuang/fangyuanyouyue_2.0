package com.fangyuanyouyue.user.controller;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.user.model.UserInfo;
import com.fangyuanyouyue.user.service.SchedualGoodsService;
import com.fangyuanyouyue.user.service.UserAddressInfoService;
import com.fangyuanyouyue.user.service.UserInfoExtService;
import com.fangyuanyouyue.user.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/userFeign")
@Api(description = "用户系统外部调用Controller",hidden = true)
@RefreshScope
public class FeignController  extends BaseController {

    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private UserAddressInfoService userAddressInfoService;
    @Autowired
    private SchedualGoodsService schedualGoodsService;//调用其他service时用


    @ApiOperation(value = "验证用户", notes = "验证用户",hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/verifyUserById")
    @ResponseBody
    public BaseResp verifyUserById(Integer userId) throws IOException {
        try {
            log.info("----》验证用户《----");
            if(userId == null){
                return toError("用户ID不能为空！");
            }
            UserInfo userInfo=userInfoService.selectByPrimaryKey(userId);
            if(userInfo==null){
                return toError("登录超时，请重新登录！");
            }
            if(userInfo.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
//            BaseClientResult result = new BaseClientResult(Status.YES.getValue(), "验证用户成功！");
//            result.put("userInfo",userInfo);
            return toSuccess(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "根据手机号验证用户", notes = "根据手机号验证用户",hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/verifyUserByPhone")
    @ResponseBody
    public BaseResp verifyUserByPhone(String phone) throws IOException {
        try {
            log.info("----》根据手机号验证用户《----");
            if(StringUtils.isEmpty(phone)){
                return toError("手机号不能为空！");
            }
            UserInfo userInfo=userInfoService.getUserByPhone(phone);
            if(userInfo==null){
                return toError("登录超时，请重新登录！");
            }
            if(userInfo.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
//            BaseClientResult result = new BaseClientResult(Status.YES.getValue(), "根据手机号验证用户成功！");
//            result.put("userInfo",userInfo);
//            return toResult(result);
            return toSuccess(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "根据三方唯一识别号获取用户", notes = "根据三方唯一识别号获取用户",hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unionId", value = "三方唯一识别号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1微信 2QQ 3微博", required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/verifyUserByUnionId")
    @ResponseBody
    public BaseResp verifyUserByUnionId(String unionId,Integer type) throws IOException {
        try {
            log.info("----》根据三方唯一识别号获取用户《----");
            if(StringUtils.isEmpty(unionId)){
                return toError("唯一识别号不能为空！");
            }
            UserInfo userInfo = userInfoService.getUserByUnionId(unionId,type);
            if(userInfo==null){
                return toError("登录超时，请重新登录！");
            }
            if(userInfo.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
//            BaseClientResult result = new BaseClientResult(Status.YES.getValue(), "根据三方唯一识别号获取用户成功！");
//            result.put("userInfo",userInfo);
//            return toResult(result);
            return toSuccess(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "用户是否官方认证", notes = "用户是否官方认证",hidden = true)
    @PostMapping(value = "/userIsAuth")
    @ResponseBody
    public BaseResp userIsAuth(Integer userId) throws IOException {
        try {
            log.info("----》用户是否官方认证《----");
            if(userId == null){
                return toError("用户ID不能为空！");
            }
            UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
            if(userInfo==null){
                return toError("登录超时，请重新登录！");
            }
            if(userInfo.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            boolean isAuth = userInfoExtService.userIsAuth(userId);
            return toSuccess(isAuth);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "验证支付密码", notes = "验证支付密码",hidden = true)
    @PostMapping(value = "/verifyPayPwd")
    @ResponseBody
    public BaseResp verifyPayPwd(Integer userId,String payPwd) throws IOException {
        try {
            log.info("----》验证支付密码《----");
            log.info("参数：userId：" + userId + ",payPwd："+ payPwd);
            if(userId == null){
                return toError("用户ID不能为空！");
            }
            UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
            if(userInfo==null){
                return toError("登录超时，请重新登录！");
            }
            if(userInfo.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            if(StringUtils.isEmpty(payPwd)){
                return toError("支付密码不能为空！");
            }
            boolean verifyPayPwd = userInfoExtService.verifyPayPwd(userId,payPwd);
            return toSuccess(verifyPayPwd);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "是否实名认证", notes = "是否实名认证",hidden = true)
    @PostMapping(value = "/isAuth")
    @ResponseBody
    public BaseResp isAuth(Integer userId) throws IOException {
        try {
            log.info("----》是否实名认证《----");
            if(userId == null){
                return toError("用户ID不能为空！");
            }
            UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
            if(userInfo==null){
                return toError("登录超时，请重新登录！");
            }
            if(userInfo.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            boolean isAuth = userInfoExtService.isAuth(userId);
            return toSuccess(isAuth);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "A是否关注用户B", notes = "A是否关注用户B",hidden = true)
    @PostMapping(value = "/isFans")
    @ResponseBody
    public BaseResp isFans(Integer userId,Integer toUserId) throws IOException {
        try {
            log.info("----》A是否关注用户B《----");
            log.info("参数：userId:"+userId+",toUserId:"+toUserId);
            if(userId == null){
                return toError("用户ID不能为空！");
            }
            if(toUserId == null){
                return toError("被关注用户ID不能为空！");
            }
            UserInfo userA = userInfoService.selectByPrimaryKey(userId);
            if(userA==null){
                return toError("登录超时，请重新登录！");
            }
            if(userA.getStatus() == 2){
                return toError("您的账号已被冻结，请联系管理员！");
            }
            boolean isFans = userInfoExtService.isFans(userId,toUserId);
            return toSuccess(isFans);
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }
}
