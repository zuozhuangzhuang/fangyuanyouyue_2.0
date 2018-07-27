package com.fangyuanyouyue.forum.utils;

/**
 * 手机验证码session的key
 * @author WuZhimin
 *
 */
public enum PhoneCode {
	
	
	//0注册，1找回密码，2设置/修改支付密码，3验证旧手机，4绑定新手机
	TYPE_REGIST(0),TYPE_FINDPWD(1),TYPE_SET_PAY_PWD(2),TYPE_OLD_PHONE(3),TYPE_NEW_PHONE(4),TYPE_AUTH(5),
	KEY_REGIST("registCode"), KEY_FINDPWD("findPwdCode"),KEY_SET_PAY_PWD("setPayPwd"),KEY_OLD_PHONE("oldPhone"),KEY_NEW_PHONE("newPhone");

	private Integer code;
	private String value;

	PhoneCode() {
	}


	PhoneCode(Integer code) {
		this.code = code;
	}

	PhoneCode(String value) {
		this.value = value;
	}

	PhoneCode(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public Integer getCode(){
		return code;
	}

	public String getValue(){
		return value;
	}
}
