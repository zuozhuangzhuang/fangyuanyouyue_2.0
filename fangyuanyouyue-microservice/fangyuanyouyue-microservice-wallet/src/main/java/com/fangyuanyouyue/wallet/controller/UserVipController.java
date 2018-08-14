package com.fangyuanyouyue.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.SchedualRedisService;
import com.fangyuanyouyue.wallet.service.SchedualUserService;
import com.fangyuanyouyue.wallet.service.UserVipService;
import com.fangyuanyouyue.wallet.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;

@Controller
@RequestMapping(value = "/userVip")
@Api(description = "用户会员系统Controller")
@RefreshScope
public class UserVipController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private WalletService walletService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private UserVipService userVipService;

    @ApiOperation(value = "开通/续费会员", notes = "(void)开通/续费会员",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vipLevel", value = "会员等级 1铂金会员 2至尊会员",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "vipType", value = "会员类型 1一个月 2三个月 3一年会员",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 1开通 2续费",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "支付方式  1微信 2支付宝 3余额", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateMebber")
    @ResponseBody
    public BaseResp updateMebber(WalletParam param) throws IOException {
        try {
            log.info("----》开通/续费会员《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            String verifyUser = schedualUserService.verifyUserById(userId);
            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
            if(jsonObject != null && (Integer)jsonObject.get("code") != 0){
                return toError(jsonObject.getString("report"));
            }
            if(param.getVipLevel()==null){
                return toError("会员等级不能为空！");
            }
            if(param.getVipType() == null){
                return toError("会员类型不能为空！");
            }
            if(param.getType() == null){
                return toError("类型不能为空！");
            }
            //TODO 开通/续费会员
            userVipService.updateMebber(userId,param.getVipLevel(),param.getVipType(),param.getType());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


}
