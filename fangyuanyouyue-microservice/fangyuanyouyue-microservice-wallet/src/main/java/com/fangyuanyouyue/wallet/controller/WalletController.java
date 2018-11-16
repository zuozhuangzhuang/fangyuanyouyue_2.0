package com.fangyuanyouyue.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.fangyuanyouyue.base.enums.ReCode;
import com.fangyuanyouyue.base.enums.Status;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.model.WxPayResult;
import com.fangyuanyouyue.base.util.ParseReturnValue;
import com.fangyuanyouyue.base.util.WechatUtil.WXPayUtil;
import com.fangyuanyouyue.base.util.alipay.util.AlipayNotify;
import com.fangyuanyouyue.wallet.dto.BillMonthDto;
import com.fangyuanyouyue.wallet.dto.UserBalanceDto;
import com.fangyuanyouyue.wallet.dto.UserCouponDto;
import com.fangyuanyouyue.wallet.dto.WalletDto;
import com.fangyuanyouyue.wallet.param.WalletParam;
import com.fangyuanyouyue.wallet.service.SchedualRedisService;
import com.fangyuanyouyue.wallet.service.SchedualUserService;
import com.fangyuanyouyue.wallet.service.UserCouponService;
import com.fangyuanyouyue.wallet.service.WalletService;
import com.snowalker.lock.redisson.RedissonLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/wallet")
@Api(description = "钱包系统Controller")
@RefreshScope
public class WalletController extends BaseController{
    protected Logger log = Logger.getLogger(this.getClass());
    protected Logger wechatLog = Logger.getLogger(this.getClass());
    @Autowired
    private WalletService walletService;
    @Autowired
    private SchedualUserService schedualUserService;
    @Autowired
    private SchedualRedisService schedualRedisService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private RedissonLock redissonLock;

    @ApiOperation(value = "充值", notes = "(void)充值",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "充值金额",  required = true,dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "充值方式 1微信 2支付宝  4小程序",required = true, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/recharge")
    @ResponseBody
    public BaseResp recharge(WalletParam param) throws IOException {
        try {
            log.info("----》充值《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
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
            //充值
            Object payInfo = walletService.recharge(userId, param.getAmount(), param.getType());
            return toSuccess(payInfo);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }



    @ApiOperation(value = "充值回调接口", notes = "充值微信回调", response = BaseResp.class)
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
                boolean result = walletService.updateOrder(wpr.getOutTradeNo(), wpr.getTransactionId(),1);

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

    @ApiOperation(value = "充值支付宝回调接口", notes = "充值支付宝回调", response = BaseResp.class,hidden = true)
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
                    boolean result = walletService.updateOrder(out_trade_no,trade_no,2);
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
                    boolean result = walletService.updateOrder(out_trade_no, trade_no,2);
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

    @ApiOperation(value = "提现", notes = "(void)提现",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "amount", value = "提现金额",  required = true,dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "提现方式 1微信 2支付宝",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "account", value = "支付宝账号(支付宝必填)",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "realName", value = "真实姓名(支付宝必填)",required = false, dataType = "String", paramType = "query"),
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
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(param.getAmount()==null || param.getAmount().doubleValue()==0 ){
                return toError("提现金额不能为空！");
            }
            if(param.getType() == null){
                return toError("提现方式不能为空！");
            }
            if(param.getAmount().doubleValue() < 1){
                return toError("提现金额不能少于1元！");
            }
            if(param.getType().equals(Status.PAY_TYPE_ALIPAY.getValue())){
                if(StringUtils.isEmpty(param.getAccount())){
                    return toError("请输入支付宝账号！");
                }
                if(StringUtils.isEmpty(param.getRealName())){
                    return toError("真实姓名不能为空！");
                }
            }
            if(StringUtils.isEmpty(param.getPayPwd())){
                return toError("支付密码为空！");
            }
            boolean lock = redissonLock.lock("withdraw"+userId,10);
            if(!lock) {
                Log.info("分布式锁获取失败");
                throw new ServiceException("提现失败！");
            }
            //提现
            walletService.withdrawDeposit(userId,param.getAmount(),param.getType(),param.getAccount(),param.getRealName(),param.getPayPwd());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }finally {
            try{
                Integer userId = (Integer)schedualRedisService.get(param.getToken());
                redissonLock.release("withdraw"+userId);
            }catch (Exception e){
                e.printStackTrace();
            }
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
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            //获取用户钱包信息
            WalletDto wallet = walletService.getWallet(userId);
            return toSuccess(wallet);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "修改支付密码", notes = "(void)修改支付密码",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "payPwd", value = "旧支付密码", required = true, dataType = "String", paramType = "query"),
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
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }
            if(StringUtils.isEmpty(param.getNewPwd())){
                return toError("新支付密码不能为空！");
            }
            //修改支付密码
            walletService.updatePayPwd(userId,param.getPayPwd(),param.getNewPwd());
            return toSuccess();
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "余额账单列表", notes = "(UserBalanceDto)余额账单列表",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始页",  required = true,dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数",required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期，例如：2018-08",required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "订单类型 1收入 2支出 3退款",required = false, dataType = "int", paramType = "query")
    })
    @PostMapping(value = "/billList")
    @ResponseBody
    public BaseResp billList(WalletParam param) throws IOException {
        try {
            log.info("----》余额账单《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }

            if(param.getStart() == null || param.getStart() < 0){
                return toError("起始页数错误！");
            }
            if(param.getLimit() == null || param.getLimit() < 1){
                return toError("每页个数错误！");
            }
            //余额账单
            List<UserBalanceDto> dtoList = walletService.billList(userId, param.getStart(), param.getLimit(), param.getType(),param.getDate());
            return toSuccess(dtoList);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "余额账单详情", notes = "(UserBalanceDto)余额账单详情",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单号",  required = true,dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/billDetail")
    @ResponseBody
    public BaseResp billDetail(WalletParam param) throws IOException {
        try {
            log.info("----》余额账单详情《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }

            if(param.getOrderNo() == null){
                return toError("订单号不能为空！");
            }
            //余额账单详情
            UserBalanceDto dto = walletService.billDetail(userId, param.getOrderNo());
            return toSuccess(dto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

    @ApiOperation(value = "按照月份获取用户总收支", notes = "(BillMonthDto)按照月份获取用户总收支",response = BaseResp.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期，例如：2018-08",  required = true,dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/monthlyBalance")
    @ResponseBody
    public BaseResp monthlyBalance(WalletParam param) throws IOException {
        try {
            log.info("----》按照月份获取用户总收支《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }

            if(StringUtils.isEmpty(param.getDate())){
                return toError("日期不能为空！");
            }
            //按照月份获取用户总收支
            BillMonthDto dto = walletService.monthlyBalance(userId, param.getDate());
            return toSuccess(dto);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }


    @ApiOperation(value = "获取用户的优惠券列表", notes = "(UserCouponDto)根据用户id获取优惠券列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/getUserCouponList")
    @ResponseBody
    public BaseResp getUserCouponList(WalletParam param) throws IOException {
        try {
            log.info("----》根据用户id获取优惠券列表《----");
            log.info("参数："+param.toString());
            //验证用户
            if(StringUtils.isEmpty(param.getToken())){
                return toError("用户token不能为空！");
            }
            Integer userId = (Integer)schedualRedisService.get(param.getToken());
            BaseResp parseReturnValue = ParseReturnValue.getParseReturnValue(schedualUserService.verifyUserById(userId));
            if(!parseReturnValue.getCode().equals(ReCode.SUCCESS.getValue())){
                return toError(parseReturnValue.getCode(),parseReturnValue.getReport());
            }

            //根据用户id获取优惠券列表
            List<UserCouponDto> listByUserId = userCouponService.getListByUserId(userId);
            return toSuccess(listByUserId);
        } catch (ServiceException e) {
            e.printStackTrace();
            return toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return toError("系统繁忙，请稍后再试！");
        }
    }

}
