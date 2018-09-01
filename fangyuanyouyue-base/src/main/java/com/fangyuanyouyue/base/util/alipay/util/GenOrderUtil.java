package com.fangyuanyouyue.base.util.alipay.util;

import com.fangyuanyouyue.base.util.alipay.config.AlipayConfig;
import com.fangyuanyouyue.base.util.alipay.sign.RSA;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.net.URLEncoder;

public class GenOrderUtil {

	protected Logger log = Logger.getLogger(this.getClass());
//	public static void main(String[] args) {
//		try {
//		 String hah=new GenOrderUtil().getOrder("", "xaiofangyuan", "xiaofangyuan", "hahasds", new BigDecimal(11));
//			System.out.println(hah);
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//	}

	/**
	 * 
	 * @param orderNo
	 *            订单号
	 * @param title
	 *            标题
	 * @param desc
	 *            描述
	 * @param notify
	 *            后台通知地址
	 * @param price
	 *            订单金额
	 * @return
	 * @throws Exception
	 */
	public String getOrder(String orderNo, String title, String desc,
			String notify, BigDecimal price) throws Exception {

		String info = "";
		String amount = price.setScale(2, BigDecimal.ROUND_HALF_UP)+""; //四舍五入 + "";
		// amount = "0.01";
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(AlipayConfig.partner);
		sb.append("\"&out_trade_no=\"");
		sb.append(orderNo);
		sb.append("\"&subject=\"");
		sb.append(title);
		sb.append("\"&body=\"");
		sb.append(desc);
		sb.append("\"&total_fee=\"");
		sb.append(amount);
		sb.append("\"&notify_url=\"");
		// 网址需要做URL编码
		sb.append(notify);
		sb.append("\"&service=\"mobile.securitypay.pay"); // 固定
		sb.append("\"&_input_charset=\"utf-8");
		// sb.append("\"&return_url=\"");
		// sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		 sb.append("\"&seller_id=\"");
		 sb.append(AlipayConfig.partner);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"15m");
		sb.append("\"");
		info = sb.toString();
		String sign = RSA
				.sign(info, AlipayConfig.private_key, "utf-8");
		sign = URLEncoder.encode(sign, "utf-8");
		info += "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";

		log.info("加密完成，支付宝请求报文：" + info);

		return info;
	}

}
