package com.fangyuanyouyue.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.WechatPayDto;
import com.fangyuanyouyue.wallet.model.WxPayResult;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.SchedualOrderService;
import com.fangyuanyouyue.wallet.service.SchedualRedisService;
import com.fangyuanyouyue.wallet.service.SchedualUserService;
import com.fangyuanyouyue.wallet.service.WalletService;
import com.fangyuanyouyue.wallet.utils.PayCommonUtil;
import com.fangyuanyouyue.wallet.utils.PropertyUtil;
import com.fangyuanyouyue.wallet.utils.WechatUtil.WXPayUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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
            walletService.updateBalance(param.getUserId(),param.getAmount(),param.getType());
            return toSuccess();
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
    public BaseResp orderPayByWechat(String orderNo, BigDecimal price) throws IOException {
        try {
            log.info("----》微信支付《----");
            log.info("参数：orderNo："+orderNo+",price:"+price);
            if(StringUtils.isEmpty(orderNo)){
                return toError("订单号不能为空！");
            }
            if(price == null || price.compareTo(new BigDecimal(0))<=0){
                return toError("订单金额异常！");
            }
            //微信支付
            WechatPayDto wechatPayDto = walletService.orderPayByWechat(orderNo, price);
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
    public BaseResp orderPayByALi(Integer orderId) throws IOException {
        try {
            log.info("----》支付宝支付《----");
            log.info("参数：orderId："+orderId);
            if(orderId == null){
                return toError("订单ID不能为空！");
            }
            //支付宝支付
            walletService.orderPayByALi(orderId);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @PostMapping(value = "/notify/wechatpay")
    @ResponseBody
    public BaseResp wechatpay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            wechatLog.info("微信支付小方圆回调数据开始");


            //示例报文
            //		String xml = "<xml><appid><![CDATA[wxb4dc385f953b356e]]></appid><bank_type><![CDATA[CCB_CREDIT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1228442802]]></mch_id><nonce_str><![CDATA[1002477130]]></nonce_str><openid><![CDATA[o-HREuJzRr3moMvv990VdfnQ8x4k]]></openid><out_trade_no><![CDATA[1000000000051249]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[1269E03E43F2B8C388A414EDAE185CEE]]></sign><time_end><![CDATA[20150324100405]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1009530574201503240036299496]]></transaction_id></xml>";
            String resXml = "";
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
                //支付成功,修改订单状态
                boolean result = JSONObject.parseObject(schedualOrderService.updateOrder(wpr.getOutTradeNo(),2)).getBoolean("data");
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


            return toSuccess(resXml);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


}
