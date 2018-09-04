package com.fangyuanyouyue.base.util.wechat.pojo;

/**
 * 微信通用接口凭证
 * 
 * @author wuzhimin  2014-2-27
 * 
 */
public class JSApiDto {
	// 获取到的凭证
	private String appid;
	private String timestamp;
	private String nonceStr;
	private String signature;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "JSApiDto [appid=" + appid + ", timestamp=" + timestamp
				+ ", nonceStr=" + nonceStr + ", signature=" + signature + "]";
	}
	

}