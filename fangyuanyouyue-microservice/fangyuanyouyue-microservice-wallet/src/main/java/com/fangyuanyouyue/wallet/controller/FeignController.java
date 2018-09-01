package com.fangyuanyouyue.wallet.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.SchedualOrderService;
import com.fangyuanyouyue.wallet.service.SchedualRedisService;
import com.fangyuanyouyue.wallet.service.SchedualUserService;
import com.fangyuanyouyue.wallet.service.WalletService;
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

@Controller
@RequestMapping(value = "/walletFeign")
@Api(description = "外部调用系统Controller",hidden = true)
@RefreshScope
public class FeignController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());

    protected Logger alipayLoglog = Logger.getLogger("alipayLog");
    protected Logger wechatLog = Logger.getLogger("wechatLog");
    @Autowired
    private WalletService walletService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private SchedualOrderService schedualOrderService;


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
            walletService.updateScore(param.getUserId(),param.getScore(),param.getType());
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
    @PostMapping(value = "/addUserBalance")
    @ResponseBody
    public BaseResp addUserBalance(Integer userId,BigDecimal amount,Integer payType,Integer type, String orderNo, String title,Integer sellerId,Integer buyerId,Integer orderType) throws IOException {
        try {
            log.info("----》新增用户收支信息《----");
            //新增用户收支信息
            walletService.addUserBalance(userId,amount,payType,type,orderNo,title,orderType,sellerId,buyerId);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }




}
