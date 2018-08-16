package com.fangyuanyouyue.wallet.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.SchedualRedisService;
import com.fangyuanyouyue.wallet.service.SchedualUserService;
import com.fangyuanyouyue.wallet.service.WalletService;
import com.fangyuanyouyue.wallet.utils.PayCommonUtil;
import com.fangyuanyouyue.wallet.utils.PropertyUtil;
import com.fangyuanyouyue.wallet.utils.WechatUtil.WXPayUtil;
import io.swagger.annotations.Api;
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
    @Autowired
    private WalletService walletService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;

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
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }

    @PostMapping(value = "/orderPayByWechat")
    @ResponseBody
    public BaseResp orderPayByWechat(Integer orderId, String orderNo, BigDecimal price) throws IOException {
        try {
            log.info("----》微信支付《----");
            log.info("参数：orderId："+orderId);
            if(orderId == null){
                return toError("订单ID不能为空！");
            }
            //微信支付
            walletService.orderPayByWechat(orderId,orderNo,price);
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
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
            return toError(ReCode.FAILD.getValue(),"系统繁忙，请稍后再试！");
        }
    }


    @PostMapping(value = "/notify/wechatpay")
    @ResponseBody
    public BaseResp wechatpay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            log.info("----》微信回调《----");
//            log.info("参数：orderId："+orderId);
            String result = PayCommonUtil.reciverWx(request); // 接收到异步的参数
            Map<String, String> m = new HashMap<>();// 解析xml成map
            if (m != null && !"".equals(m))
            {
                m = WXPayUtil.xmlToMap(result);
            }
            // 过滤空 设置 TreeMap
            SortedMap<Object, Object> packageParams = new TreeMap<>();
            Iterator it = m.keySet().iterator();
            while (it.hasNext())
            {
                String parameter = (String) it.next();
                String parameterValue = m.get(parameter);
                String v = "";
                if (null != parameterValue)
                {
                    v = parameterValue.trim();
                }
                packageParams.put(parameter, v);
            }
            // 判断签名是否正确
            String resXml = "";
            if (PayCommonUtil.isTenpaySign("UTF-8", packageParams))
            {
                if ("SUCCESS".equals((String) packageParams.get("return_code")))
                {
                    // 如果返回成功
                    String mch_id = (String) packageParams.get("mch_id"); // 商户号
                    String out_trade_no = (String) packageParams.get("out_trade_no"); // 商户订单号
                    String total_fee = (String) packageParams.get("total_fee");
                    // String transaction_id = (String)
                    // packageParams.get("transaction_id"); // 微信支付订单号
                    // 查询订单 根据订单号查询订单
                    String orderId = out_trade_no.substring(0, out_trade_no.length() - PayCommonUtil.TIME.length());
//                    Orders orders = ordersMapper.selectByPrimaryKey(Integer.parseInt(orderId));

                    // 验证商户ID 和 价格 以防止篡改金额
                    if (PropertyUtil.getInstance().getProperty("WxPay.mchid").equals(mch_id)
//                            && orders != null
                        // &&
                        // total_fee.trim().toString().equals(orders.getOrderAmount())
                        // // 实际项目中将此注释删掉，以保证支付金额相等
                            )
                    {
                        /** 这里是我项目里的消费状态
                         * 1.待付款=0 2.付款完成=1
                         * 3.消费成功=2
                         * 4.取消=-1
                         * 5.发起退款=-2
                         * 6.退款成功=-3
                         * 7.退款失败=3（由于商户拒绝退款或其他原因导致退款失败）
                         */
//                        insertWxNotice(packageParams);
//                        orders.setPayWay("1"); // 变更支付方式为wx
//                        orders.setOrderState("1"); // 订单状态为已付款
//
//                        ordersMapper.updateByPrimaryKeySelective(orders); // 变更数据库中该订单状态
                        // ordersMapper.updatePayStatus(Integer.parseInt(orderId));
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else
                    {
                        resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                                + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                    }
                } else // 如果微信返回支付失败，将错误信息返回给微信
                {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[交易失败]]></return_msg>" + "</xml> ";
                }
            } else
            {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            }

            // 处理业务完毕，将业务结果通知给微信
            // ------------------------------
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
//            walletService.wechatpay();
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
