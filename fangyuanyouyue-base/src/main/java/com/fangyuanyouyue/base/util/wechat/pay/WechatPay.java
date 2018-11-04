package com.fangyuanyouyue.base.util.wechat.pay;

import com.fangyuanyouyue.base.dto.WechatPayDto;
import com.fangyuanyouyue.base.util.wechat.pay.utils.ClientCustomSSL;
import com.fangyuanyouyue.base.util.wechat.pay.utils.GetWxOrderno;
import com.fangyuanyouyue.base.util.wechat.pay.utils.MD5;
import com.fangyuanyouyue.base.util.wechat.pay.utils.RequestHandler;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class WechatPay {

	protected Logger log = Logger.getLogger(this.getClass());
	
	
	public static void main(String[] args) {
		WechatPay pay = new WechatPay();
		//pay.genOrderWeb("o5VaduLtpTIRGv7sHd5QureMkV20","VIP010101", "1", "VIP购买", "http://test.shaugnchiyiming.com/weixin/personal_center", "127.0.0.1");
//		WechatPayDto dto = pay.genOrderWeb("oteLFwc3oyWMRkXX7PrqWqtD-Z2g","TEST001", "0.01", "小方圆测试支付", "http://wechat.xiaohoukx.com/client/user", "127.0.0.1");
		WechatPayDto dto = pay.genOrderMini("onuC35RHaX-BKjwndrL-PU2IHzHE","123123123", "0.01", "小方圆测试支付", "https://miniprogram.fangyuanyouyue.com/wallet/wallet/notify/wechat", "127.0.0.1");
		System.out.println(dto);
		
		/*boolean dto = pay.refund("VI000000057","VI000000057","1","");
		System.out.println(dto);*/
		//微信支付jsApi
		/*WxPayDto tpWxPay = new WxPayDto();
		tpWxPay.setBody("商品信息");
		tpWxPay.setSpbillCreateIp("127.0.0.1");
		tpWxPay.setTotalFee("100");
		tpWxPay.setNotifyUrl("test.shaugnchiyiming.com");
		tpWxPay.setOrderId("00000052");
	    String prepayId = pay.getPrePayIdWeb(tpWxPay);
	    System.out.println("获取到的prepayId="+prepayId);
	    String sign = pay.genPayReq(prepayId);
	    System.out.println("获取到的sign="+sign);*/
		
		
	}

	/**
	 * 微信三方下单
	 * @param orderNo
	 * @param totalFee
	 * @param desc
	 * @param notify_url
	 * @param ip
	 * @return
	 */
	public WechatPayDto genOrder(String orderNo,String totalFee,String desc,String notify_url,String ip){
		totalFee = getMoney(totalFee);
		String prepayId = getPrePayId(orderNo, totalFee, desc, notify_url, ip);
		WechatPayDto wechatPayDto = new WechatPayDto();
		genPayReq(prepayId, wechatPayDto);
		return wechatPayDto;
	}

	/**
	 * 公众号下单
	 * @param openId
	 * @param orderNo
	 * @param totalFee
	 * @param desc
	 * @param notify_url
	 * @param ip
	 * @return
	 */
	public WechatPayDto genOrderWeb(String openId,String orderNo,String totalFee,String desc,String notify_url,String ip){
		totalFee = getMoney(totalFee);
		String prepayId = getPrePayIdWeb(openId,orderNo, totalFee, desc, notify_url, ip);
		WechatPayDto wechatPayDto = new WechatPayDto();
		genPayReqWeb(prepayId, wechatPayDto);
		//生成第二次加密
	    List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appId", wechatPayDto.getAppId()));
		packageParams.add(new BasicNameValuePair("nonceStr", wechatPayDto.getNonceStr()));
		packageParams.add(new BasicNameValuePair("package","prepay_id="+wechatPayDto.getPrepayId()));
		packageParams.add(new BasicNameValuePair("signType","MD5"));
		packageParams.add(new BasicNameValuePair("timeStamp", wechatPayDto.getTimeStamp()));

		String sign = genPackageSignWeb(packageParams);
		wechatPayDto.setWebSign(sign);
		
		 
		return wechatPayDto;
	}

	/**
	 * 小程序下单
	 * @param openId
	 * @param orderNo
	 * @param totalFee
	 * @param desc
	 * @param notify_url
	 * @param ip
	 * @return
	 */
	public WechatPayDto genOrderMini(String openId,String orderNo,String totalFee,String desc,String notify_url,String ip){
		totalFee = getMoney(totalFee);
		String prepayId = getPrePayIdMini(openId,orderNo, totalFee, desc, notify_url, ip);
		WechatPayDto wechatPayDto = new WechatPayDto();
		genPayReqMini(prepayId, wechatPayDto);
		//生成第二次加密
	    List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appId", wechatPayDto.getAppId()));
		packageParams.add(new BasicNameValuePair("nonceStr", wechatPayDto.getNonceStr()));
		packageParams.add(new BasicNameValuePair("package","prepay_id="+wechatPayDto.getPrepayId()));
		packageParams.add(new BasicNameValuePair("signType","MD5"));
		packageParams.add(new BasicNameValuePair("timeStamp", wechatPayDto.getTimeStamp()));

		String sign = genPackageSignMini(packageParams);
		wechatPayDto.setWebSign(sign);


		return wechatPayDto;
	}


	/**
	 * 三方支付获取prepayId
	 * @param orderNo
	 * @param totalFee
	 * @param desc
	 * @param notify_url
	 * @param ip
	 * @return
	 */
	private String getPrePayId(String orderNo,String totalFee,String desc,String notify_url,String ip) {
		
		String appId = WechatPayConfig.APP_ID;
		// 1 参数
		// 订单号
	//	String orderId = orderNo;
		// 附加数据 原样返回
	//	String attach = "";
		// 总金额以分为单位，不带小数点
	//	String totalFee = totalFee;
		// 订单生成的机器 IP
		String spbill_create_ip = ip;
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
	//	String notify_url = notify_url;

		// ---必须参数
		// 商户号
		String mch_id = WechatPayConfig.MCH_ID;
		// 商品描述根据情况修改
		String body = desc;
		// 商户订单号
	//	String out_trade_no = orderId;
		// 随机字符串
		String	nonceStr = genNonceStr();

        List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appid", appId));
		packageParams.add(new BasicNameValuePair("body", body));
		packageParams.add(new BasicNameValuePair("mch_id", mch_id));
		packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
		packageParams.add(new BasicNameValuePair("notify_url", notify_url));
		packageParams.add(new BasicNameValuePair("out_trade_no",orderNo));
		packageParams.add(new BasicNameValuePair("spbill_create_ip",spbill_create_ip));
		packageParams.add(new BasicNameValuePair("total_fee", totalFee));
		packageParams.add(new BasicNameValuePair("trade_type", "APP"));

		String sign = genPackageSign(packageParams);
		packageParams.add(new BasicNameValuePair("sign", sign));

	   String xmlstring =toXml(packageParams);
		
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		String prepayId =  GetWxOrderno.getPayNo(createOrderURL, xmlstring);
		
		return prepayId;
		
	}

	
	/**
	 * 公众号获取prepayId
	 * @param openId
	 * @param orderNo
	 * @param totalFee
	 * @param desc
	 * @param notify_url
	 * @param ip
	 * @return
	 */
	private String getPrePayIdWeb(String openId,String orderNo,String totalFee,String desc,String notify_url,String ip) {
		
		String appId = WechatPayConfig.APP_ID_WEB;
		// 1 参数
		// 订单号
	//	String orderId = orderNo;
		// 附加数据 原样返回
	//	String attach = "";
		// 总金额以分为单位，不带小数点
	//	String totalFee = totalFee;
		// 订单生成的机器 IP
		String spbill_create_ip = ip;
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
	//	String notify_url = notify_url;

		// ---必须参数
		// 商户号
		String mch_id = WechatPayConfig.MCH_ID_WEB;
		// 商品描述根据情况修改
		String body = desc;
		// 商户订单号
	//	String out_trade_no = orderId;
		// 随机字符串
		String	nonceStr = genNonceStr();

        List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appid", appId));
		packageParams.add(new BasicNameValuePair("body", body));
		packageParams.add(new BasicNameValuePair("mch_id", mch_id));
		packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
		packageParams.add(new BasicNameValuePair("notify_url", notify_url));
		packageParams.add(new BasicNameValuePair("openid", openId));
		packageParams.add(new BasicNameValuePair("out_trade_no",orderNo));
		packageParams.add(new BasicNameValuePair("spbill_create_ip",spbill_create_ip));
		packageParams.add(new BasicNameValuePair("total_fee", totalFee));
		packageParams.add(new BasicNameValuePair("trade_type", "JSAPI"));

		String sign = genPackageSignWeb(packageParams);
		packageParams.add(new BasicNameValuePair("sign", sign));

	   String xmlstring =toXml(packageParams);
		
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		String prepayId =  GetWxOrderno.getPayNo(createOrderURL, xmlstring);
		
		return prepayId;
		
	}

	/**
	 * 小程序获取prepayId
	 * @param openId
	 * @param orderNo
	 * @param totalFee
	 * @param desc
	 * @param notify_url
	 * @param ip
	 * @return
	 */
	private String getPrePayIdMini(String openId,String orderNo,String totalFee,String desc,String notify_url,String ip) {

		String appId = WechatPayConfig.APP_ID_MINI;
		// 1 参数
		// 订单号
	//	String orderId = orderNo;
		// 附加数据 原样返回
	//	String attach = "";
		// 总金额以分为单位，不带小数点
	//	String totalFee = totalFee;
		// 订单生成的机器 IP
		String spbill_create_ip = ip;
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
	//	String notify_url = notify_url;

		// ---必须参数
		// 商户号
		String mch_id = WechatPayConfig.MCH_ID_MINI;
		// 商品描述根据情况修改
		String body = desc;
		// 商户订单号
	//	String out_trade_no = orderId;
		// 随机字符串
		String	nonceStr = genNonceStr();

        List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appid", appId));
		packageParams.add(new BasicNameValuePair("body", body));
		packageParams.add(new BasicNameValuePair("mch_id", mch_id));
		packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
		packageParams.add(new BasicNameValuePair("notify_url", notify_url));
		packageParams.add(new BasicNameValuePair("openid", openId));
		packageParams.add(new BasicNameValuePair("out_trade_no",orderNo));
		packageParams.add(new BasicNameValuePair("spbill_create_ip",spbill_create_ip));
		packageParams.add(new BasicNameValuePair("total_fee", totalFee));
		packageParams.add(new BasicNameValuePair("trade_type", "JSAPI"));

		String sign = genPackageSignMini(packageParams);
		packageParams.add(new BasicNameValuePair("sign", sign));

	   String xmlstring =toXml(packageParams);

		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

		String prepayId =  GetWxOrderno.getPayNo(createOrderURL, xmlstring);

		return prepayId;

	}
	
	
	//申请退款
	public boolean refund(String orderNo,String refundNo,String totalFee,String certPath) {
		
		totalFee = getMoney(totalFee);
		
		String appId = WechatPayConfig.APP_ID;
		// 1 参数
		// 订单号
		// ---必须参数
		// 商户号
		String mch_id = WechatPayConfig.MCH_ID;
		// 商品描述根据情况修改
		// 商户订单号
	//	String out_trade_no = orderId;
		// 随机字符串
		String	nonceStr = genNonceStr();
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appId);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonceStr);
		packageParams.put("out_trade_no", orderNo);
		packageParams.put("out_refund_no",refundNo);
		packageParams.put("total_fee", totalFee);
		packageParams.put("refund_fee", totalFee);
		packageParams.put("op_user_id", mch_id);

		RequestHandler reqHandler = new RequestHandler(
				null, null);
		reqHandler.init(WechatPayConfig.APP_ID, WechatPayConfig.APP_SECRET, WechatPayConfig.API_KEY);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appId + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonceStr
				+ "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>"
				+ "<out_trade_no>" + orderNo + "</out_trade_no>"
				+ "<out_refund_no>" + refundNo + "</out_refund_no>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<refund_fee>" + totalFee + "</refund_fee>"
				+ "<op_user_id>" + mch_id + "</op_user_id>" + "</xml>";

		
/*
        List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appid", appId));
		packageParams.add(new BasicNameValuePair("mch_id", mch_id));
		packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
		packageParams.add(new BasicNameValuePair("out_trade_no",orderNo));
		packageParams.add(new BasicNameValuePair("out_refund_no",orderNo));
		packageParams.add(new BasicNameValuePair("total_fee", totalFee));
		packageParams.add(new BasicNameValuePair("refund_fee", totalFee));
		packageParams.add(new BasicNameValuePair("op_user_id", mch_id));

		String sign = genPackageSign(packageParams);
		packageParams.add(new BasicNameValuePair("sign", sign));

	   String xmlstring =toXml(packageParams);
		*/
		String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		String s = null;
		String resultCode = null;
		try {
			s= ClientCustomSSL.doRefund(createOrderURL, xml,certPath,mch_id);
			Map map = doXMLParse(s);
			String reCode = (String) map.get("return_code");
			if("SUCCESS".equals(reCode)){
				resultCode = (String) map.get("result_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(resultCode!=null&&"SUCCESS".equals(resultCode)){
			return true;
		}
		
		return false;
		
	}
	
	
	
	//申请退款
	public boolean refundWeb(String orderNo,String refundNo,String totalFee,String certPath) {
		
		totalFee = getMoney(totalFee);
		
		String appId = WechatPayConfig.APP_ID_WEB;
		// 1 参数
		// 订单号
		// ---必须参数
		// 商户号
		String mch_id = WechatPayConfig.MCH_ID_WEB;
		// 商品描述根据情况修改
		// 商户订单号
	//	String out_trade_no = orderId;
		// 随机字符串
		String	nonceStr = genNonceStr();
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appId);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonceStr);
		packageParams.put("out_trade_no", orderNo);
		packageParams.put("out_refund_no",refundNo);
		packageParams.put("total_fee", totalFee);
		packageParams.put("refund_fee", totalFee);
		packageParams.put("op_user_id", mch_id);

		RequestHandler reqHandler = new RequestHandler(
				null, null);
		reqHandler.init(WechatPayConfig.APP_ID_WEB, WechatPayConfig.APP_SECRET_WEB, WechatPayConfig.API_KEY_WEB);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appId + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonceStr
				+ "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>"
				+ "<out_trade_no>" + orderNo + "</out_trade_no>"
				+ "<out_refund_no>" + refundNo + "</out_refund_no>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<refund_fee>" + totalFee + "</refund_fee>"
				+ "<op_user_id>" + mch_id + "</op_user_id>" + "</xml>";

		
/*
        List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appid", appId));
		packageParams.add(new BasicNameValuePair("mch_id", mch_id));
		packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
		packageParams.add(new BasicNameValuePair("out_trade_no",orderNo));
		packageParams.add(new BasicNameValuePair("out_refund_no",orderNo));
		packageParams.add(new BasicNameValuePair("total_fee", totalFee));
		packageParams.add(new BasicNameValuePair("refund_fee", totalFee));
		packageParams.add(new BasicNameValuePair("op_user_id", mch_id));

		String sign = genPackageSign(packageParams);
		packageParams.add(new BasicNameValuePair("sign", sign));

	   String xmlstring =toXml(packageParams);
		*/
		String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		String s = null;
		String resultCode = null;
		try {
			s= ClientCustomSSL.doRefund(createOrderURL, xml,certPath,mch_id);
			Map map = doXMLParse(s);
			String reCode = (String) map.get("return_code");
			if("SUCCESS".equals(reCode)){
				resultCode = (String) map.get("result_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(resultCode!=null&&"SUCCESS".equals(resultCode)){
			return true;
		}
		
		return false;
		
	}
	
	

	//生成订单请求
	private String genPayReq(String prepayId,WechatPayDto dto) {

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", WechatPayConfig.APP_ID));
		dto.setAppId(WechatPayConfig.APP_ID);
		String nonceStr = genNonceStr();
		signParams.add(new BasicNameValuePair("noncestr", nonceStr));
		dto.setNonceStr(nonceStr);
		signParams.add(new BasicNameValuePair("package", "Sign=WXPay"));
		dto.setPackageValue("Sign=WXPay");
		signParams.add(new BasicNameValuePair("partnerid", WechatPayConfig.MCH_ID));
		dto.setPartnerId(WechatPayConfig.MCH_ID);
		signParams.add(new BasicNameValuePair("prepayid", prepayId));
		dto.setPrepayId(prepayId);
		String timeStamp = String.valueOf(genTimeStamp());
		signParams.add(new BasicNameValuePair("timestamp", timeStamp));
		dto.setTimeStamp(timeStamp);
		String sign = genAppSign(signParams);
		dto.setSign(sign);
		return sign;
	}
	

	//生成订单请求
	private String genPayReqWeb(String prepayId,WechatPayDto dto) {

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", WechatPayConfig.APP_ID_WEB));
		dto.setAppId(WechatPayConfig.APP_ID_WEB);
		String nonceStr = genNonceStr();
		signParams.add(new BasicNameValuePair("noncestr", nonceStr));
		dto.setNonceStr(nonceStr);
		signParams.add(new BasicNameValuePair("package", "Sign=WXPay"));
		dto.setPackageValue("Sign=WXPay");
		signParams.add(new BasicNameValuePair("partnerid", WechatPayConfig.MCH_ID_WEB));
		dto.setPartnerId(WechatPayConfig.MCH_ID_WEB);
		signParams.add(new BasicNameValuePair("prepayid", prepayId));
		dto.setPrepayId(prepayId);
		String timeStamp = String.valueOf(genTimeStamp());
		signParams.add(new BasicNameValuePair("timestamp", timeStamp));
		dto.setTimeStamp(timeStamp);
		String sign = genAppSign(signParams);
		dto.setSign(sign);
		return sign;
	}

	//小程序生成订单请求
	private String genPayReqMini(String prepayId,WechatPayDto dto) {

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", WechatPayConfig.APP_ID_MINI));
		dto.setAppId(WechatPayConfig.APP_ID_MINI);
		String nonceStr = genNonceStr();
		signParams.add(new BasicNameValuePair("noncestr", nonceStr));
		dto.setNonceStr(nonceStr);
		signParams.add(new BasicNameValuePair("package", "Sign=WXPay"));
		dto.setPackageValue("Sign=WXPay");
		signParams.add(new BasicNameValuePair("partnerid", WechatPayConfig.MCH_ID_MINI));
		dto.setPartnerId(WechatPayConfig.MCH_ID_MINI);
		signParams.add(new BasicNameValuePair("prepayid", prepayId));
		dto.setPrepayId(prepayId);
		String timeStamp = String.valueOf(genTimeStamp());
		signParams.add(new BasicNameValuePair("timestamp", timeStamp));
		dto.setTimeStamp(timeStamp);
		String sign = genAppSign(signParams);
		dto.setSign(sign);
		return sign;
	}

	
	//获取签名
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(WechatPayConfig.API_KEY);
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return packageSign;
	}
	
	//获取签名
	private String genPackageSignWeb(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(WechatPayConfig.API_KEY_WEB);
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return packageSign;
	}

	//小程序获取签名
	private String genPackageSignMini(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(WechatPayConfig.API_KEY_MINI);
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return packageSign;
	}
	
	
	
	
	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");


			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		return sb.toString();
	}
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	
	
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(WechatPayConfig.API_KEY);
		System.out.println(sb.toString());
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return appSign;
	}
	
	
	/**
	 * 元转换成分
	 * @param amount
	 * @return
	 */
	private String getMoney(String amount) {
		if(amount==null){
			return "";
		}
		// 金额转化为分为单位
		String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额  
        int index = currency.indexOf(".");  
        int length = currency.length();  
        Long amLong = 0L;
        if(index == -1){  
            amLong = Long.valueOf(currency+"00");  
        }else if(length - index >= 3){  
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
        }else if(length - index == 2){  
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
        }else{  
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
        }  
        return amLong.toString(); 
	}
	
	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * 
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		Map m = new HashMap();
		InputStream in = String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}

			m.put(k, v);
		}

		// 关闭流
		in.close();

		return m;
	}

	/**
	 * 获取子结点的xml
	 * 
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}
	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
	
}
