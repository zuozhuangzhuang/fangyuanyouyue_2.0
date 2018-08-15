package com.fangyuanyouyue.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.WalletDto;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.SchedualRedisService;
import com.fangyuanyouyue.wallet.service.SchedualUserService;
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
@RequestMapping(value = "/wallet")
@Api(description = "钱包系统Controller")
@RefreshScope
public class WalletController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private WalletService walletService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;

    @ApiOperation(value = "充值", notes = "(void)充值",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "充值金额",  required = true,dataType = "BigDecimal", paramType = "query"),
            //TODO 3公众号微信 和 1微信支付 有啥区别
            @ApiImplicitParam(name = "type", value = "充值方式 1微信 2支付宝（3公众号微信）",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/recharge")
    @ResponseBody
    public BaseResp recharge(WalletParam param) throws IOException {
        try {
            log.info("----》充值《----");
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
            if(param.getAmount()==null){
                return toError("充值金额不能为空！");
            }
            if(param.getAmount().compareTo(new BigDecimal(0)) < 0){
                return toError("充值金额错误！");
            }
            if(param.getType() == null){
                return toError("充值方式不能为空！");
            }
            //TODO 充值
            walletService.recharge(userId,param.getAmount(),param.getType());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "提现", notes = "(void)提现",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "提现金额",  required = true,dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "提现方式 1微信 2支付宝 ",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "account", value = "支付宝账号(如果体现方试是支付宝，必填)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "realName", value = "真实姓名",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码",required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/withdrawDeposit")
    @ResponseBody
    public BaseResp withdrawDeposit(WalletParam param) throws IOException {
        try {
            log.info("----》提现《----");
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
            if(param.getAmount()==null || param.getAmount().doubleValue()==0 ){
                return toError("提现金额不能为空！");
            }
            if(param.getType() == null){
                return toError("提现方式不能为空！");
            }
            if(param.getAmount().doubleValue() < 100){
                return toError("提现金额不能少于100！");
            }
            if("0".equals(param.getType()) && StringUtils.isEmpty(param.getAccount())){
                return toError("请输入支付宝账号！");
            }
            if("0".equals(param.getType()) && StringUtils.isEmpty(param.getRealName())){
                return toError("真实姓名不能为空！");
            }
            //TODO 提现
            walletService.withdrawDeposit(userId,param.getAmount(),param.getType(),param.getAccount(),param.getRealName(),param.getPayPwd());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "获取用户钱包信息", notes = "(WalletDto)获取用户钱包信息",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/getWallet")
    @ResponseBody
    public BaseResp getWallet(WalletParam param) throws IOException {
        try {
            log.info("----》获取用户钱包信息《----");
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
            //获取用户钱包信息
            WalletDto wallet = walletService.getWallet(userId);
            return toSuccess(wallet);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "修改支付密码", notes = "(void)修改支付密码",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "旧支付密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "newPwd", value = "新支付密码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updatePayPwd")
    @ResponseBody
    public BaseResp updatePayPwd(WalletParam param) throws IOException {
        try {
            log.info("----》修改支付密码《----");
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
            if(StringUtils.isEmpty(param.getPayPwd())){
                return toError(ReCode.FAILD.getValue(),"旧支付密码不能为空！");
            }
            if(StringUtils.isEmpty(param.getNewPwd())){
                return toError(ReCode.FAILD.getValue(),"新支付密码不能为空！");
            }
            //修改支付密码
            walletService.updatePayPwd(userId,param.getPayPwd(),param.getNewPwd());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


//    @ApiOperation(value = "支付", notes = "(void)支付",response = BaseResp.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "orderId", value = "订单ID",  required = true,dataType = "BigDecimal", paramType = "query"),
//            @ApiImplicitParam(name = "type", value = "支付方式 1支付宝 2微信 3余额支付",required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "orderType", value = "订单类型 1商品下单 2开通会员 3商品压价 4商品鉴定 5全民鉴定",required = true, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "payPwd", value = "支付密码",required = true, dataType = "String", paramType = "query")
//    })
//    @PostMapping(value = "/payOrder")
//    @ResponseBody
//    public BaseResp payOrder(WalletParam param) throws IOException {
//        try {
//            log.info("----》支付《----");
//            log.info("参数："+param.toString());
//            //验证用户
//            if(StringUtils.isEmpty(param.getToken())){
//                return toError(ReCode.FAILD.getValue(),"用户token不能为空！");
//            }
//            Integer userId = (Integer)schedualRedisService.get(param.getToken());
//            String verifyUser = schedualUserService.verifyUserById(userId);
//            JSONObject jsonObject = JSONObject.parseObject(verifyUser);
//            if(jsonObject != null && (Integer)jsonObject.get("code") != 0){
//                return toError(jsonObject.getString("report"));
//            }
//            if(param.getOrderId==null || param.getAmount().doubleValue()==0 ){
//                return toError("提现金额不能为空！");
//            }
//            if(param.getType() == null){
//                return toError("提现方式不能为空！");
//            }
//            if(param.getAmount().doubleValue() < 100){
//                return toError("提现金额不能少于100！");
//            }
//            if("0".equals(param.getType()) && StringUtils.isEmpty(param.getAccount())){
//                return toError("请输入支付宝账号！");
//            }
//            if("0".equals(param.getType()) && StringUtils.isEmpty(param.getRealName())){
//                return toError("真实姓名不能为空！");
//            }
//            //TODO 支付
//            walletService.withdrawDeposit(userId,param.getAmount(),param.getType(),param.getAccount(),param.getRealName(),param.getPayPwd());
//            return toSuccess();
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            return toError(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
//        }
//    }

}
