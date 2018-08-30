package com.fangyuanyouyue.base.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信支付
 */
@Getter
@Setter
@ToString
public class WechatPayDto {

	private String appId;//公众账号ID

	private String partnerId;//商户号

	private String prepayId;//预支付交易会话标识

	private String packageValue;//

	private String nonceStr;//随机字符串

	private String timeStamp;//时间

	private String sign;//签名

	private String webSign;//

}