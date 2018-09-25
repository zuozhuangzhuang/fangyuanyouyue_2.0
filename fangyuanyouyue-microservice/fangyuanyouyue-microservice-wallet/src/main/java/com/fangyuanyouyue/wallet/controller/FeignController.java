package com.fangyuanyouyue.wallet.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.UserCouponDto;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping(value = "/walletFeign")
@Api(description = "外部调用系统Controller",hidden = true)
@RefreshScope
public class FeignController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private WalletService walletService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private SchedualOrderService schedualOrderService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserBehaviorService userBehaviorService;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private UserVipService userVipService;


    @PostMapping(value = "/updateScore")
    @ResponseBody
    public BaseResp updateScore(WalletParam param) throws IOException {
        try {
            log.info("----》修改积分《----");
            log.info("参数："+param.toString());
            if(param.getUserId() == null){
                return toError("用户ID不能为空！");
            }
            if(param.getScore() == null){
                return toError("积分错误！");
            }
            if(param.getType() == null){
                return toError("类型错误！");
            }
            //修改积分
            scoreService.updateScore(param.getUserId(),param.getScore(),param.getType());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @PostMapping(value = "/updateBalance")
    @ResponseBody
    public BaseResp updateBalance(WalletParam param) throws IOException {
        try {
            log.info("----》修改余额《----");
            log.info("参数："+param.toString());
            if(param.getUserId() == null){
                return toError("用户ID不能为空！");
            }
            if(param.getAmount() == null){
                return toError("修改金额错误！");
            }
            if(param.getType() == null){
                return toError("类型错误！");
            }
            //修改余额
            boolean result = walletService.updateBalance(param.getUserId(), param.getAmount(), param.getType());
            return toSuccess(result);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @PostMapping(value = "/updateCredit")
    @ResponseBody
    public BaseResp updateCredit(WalletParam param) throws IOException {
        try {
            log.info("----》修改信誉度《----");
            log.info("参数："+param.toString());
            if(param.getUserId() == null){
                return toError("用户ID不能为空！");
            }
            if(param.getCredit() == null){
                return toError("修改信誉度数值错误！");
            }
            if(param.getType() == null){
                return toError("类型错误！");
            }
            //修改信誉度
            walletService.updateCredit(param.getUserId(),param.getCredit(),param.getType());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @GetMapping(value = "/getAppraisalCount")
    @ResponseBody
    public BaseResp getAppraisalCount(Integer userId) throws IOException {
        try {
            log.info("----》获取免费鉴定次数《----");
            log.info("参数：userId："+userId);
            if(userId == null){
                return toError("用户ID不能为空！");
            }
            //获取免费鉴定次数
            Integer count = walletService.getAppraisalCount(userId);
            return toSuccess(count);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    @PostMapping(value = "/updateAppraisalCount")
    @ResponseBody
    public BaseResp updateAppraisalCount(Integer userId,Integer count) throws IOException {
        try {
            log.info("----》修改剩余免费鉴定次数《----");
            log.info("参数：userId："+userId);
            if(userId == null){
                return toError("用户ID不能为空！");
            }
            if(count == null){
                return toError("修改数值不能为空！");
            }
            //修改剩余免费鉴定次数
            walletService.updateAppraisalCount(userId,count);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @PostMapping(value = "/orderPayByWechat")
    @ResponseBody
    public BaseResp orderPayByWechat(String orderNo, BigDecimal price,String notifyUrl) throws IOException {
        try {
            log.info("----》微信支付《----");
            log.info("参数：orderNo："+orderNo+",price:"+price);
            if(StringUtils.isEmpty(orderNo)){
                return toError("订单号不能为空！");
            }
            if(price == null || price.compareTo(new BigDecimal(0))<=0){
                return toError("订单金额异常！");
            }
            if(StringUtils.isEmpty(notifyUrl)){
                return toError("回调地址不能为空！");
            }
            //微信支付
            WechatPayDto wechatPayDto = walletService.orderPayByWechat(orderNo, price,notifyUrl);
            return toSuccess(wechatPayDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @PostMapping(value = "/orderPayByWechatMini")
    @ResponseBody
    public BaseResp orderPayByWechatMini(Integer userId,String orderNo, BigDecimal price,String notifyUrl) throws IOException {
        try {
            log.info("----》微信小程序支付《----");
            log.info("参数：orderNo："+orderNo+",price:"+price);
            if(userId == null){
                return toError("用户Id不能为空！");
            }
            if(StringUtils.isEmpty(orderNo)){
                return toError("订单号不能为空！");
            }
            if(price == null || price.compareTo(new BigDecimal(0))<=0){
                return toError("订单金额异常！");
            }
            if(StringUtils.isEmpty(notifyUrl)){
                return toError("回调地址不能为空！");
            }
            //微信支付
            WechatPayDto wechatPayDto = walletService.orderPayByWechatMini(userId,orderNo, price,notifyUrl);
            return toSuccess(wechatPayDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @PostMapping(value = "/orderPayByALi")
    @ResponseBody
    public BaseResp orderPayByALi(String orderNo, BigDecimal price,String notifyUrl) throws IOException {
        try {
            log.info("----》支付宝支付《----");
            log.info("参数：orderNo："+orderNo+",price:"+price);
            if(StringUtils.isEmpty(orderNo)){
                return toError("订单号不能为空！");
            }
            if(price == null || price.compareTo(new BigDecimal(0))<=0){
                return toError("订单金额异常！");
            }
            if(StringUtils.isEmpty(notifyUrl)){
                return toError("回调地址不能为空！");
            }
            //支付宝支付
            String payInfo = walletService.orderPayByALi(orderNo, price, notifyUrl);
            return toSuccess(payInfo);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "新增用户收支信息", notes = "(void)新增用户收支信息",hidden = true)
    @PostMapping(value = "/addUserBalanceDetail")
    @ResponseBody
    public BaseResp addUserBalanceDetail(Integer userId,BigDecimal amount,Integer payType,Integer type, String orderNo, String title,Integer sellerId,Integer buyerId,Integer orderType,String payNo) throws IOException {
        try {
            log.info("----》新增用户收支信息《----");
            //新增用户收支信息
            walletService.addUserBalanceDetail(userId,amount,payType,type,orderNo,title,orderType,sellerId,buyerId,payNo);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "根据优惠券id计算价格", notes = "(BigDecimal)计算后价格",hidden = true)
    @PostMapping(value = "/getPriceByCoupon")
    @ResponseBody
    public BaseResp getPriceByCoupon(Integer userId,BigDecimal price,Integer couponId) throws IOException {
        try {
            log.info("----》根据优惠券id计算价格《----");
            log.info("参数:userId="+userId+",price="+price+",couponId="+couponId);
            if(price == null || price.compareTo(new BigDecimal(0))<=0){
                return toError("价格错误！");
            }
            if(couponId == null){
                return toError("优惠券id不能为空！");
            }
            //根据优惠券id计算价格
            BigDecimal priceByCoupon = userCouponService.getPriceByCoupon(userId,price, couponId);
            return toSuccess(priceByCoupon);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "新增用户行为", notes = "新增用户行为，并增加积分、信誉度、发送关注信息",hidden = true)
    @PostMapping(value = "/addUserBehavior")
    @ResponseBody
    public BaseResp addUserBehavior(Integer userId,Integer toUserId, Integer businessId, Integer businessType, Integer type) throws IOException {
        try {
            log.info("----》新增用户行为《----");
            if(userId == null){
                return toError("用户id不能为空！");
            }
            if(toUserId == null){
                return toError("行为对象用户id不能为空！");
            }
            if(businessId == null){
                return toError("对象id不能为空！");
            }
            if(businessType == null){
                return toError("对象类型不能为空！");
            }
            if(type == null){
                return toError("行为类型不能为空！");
            }
            //新增用户行为
            userBehaviorService.addUserBehavior(userId,toUserId,businessId,businessType,type);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "验证用户是否是会员", notes = "验证用户是否是会员",hidden = true)
    @PostMapping(value = "/isUserVip")
    @ResponseBody
    public BaseResp isUserVip(Integer userId) throws IOException {
        try {
            log.info("----》验证用户是否是会员《----");
            if(userId == null){
                return toError("用户id不能为空！");
            }
            //新增用户行为
            boolean isUserVip = userVipService.isUserVip(userId);
            return toSuccess(isUserVip);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



}
