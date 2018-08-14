package com.fangyuanyouyue.user.constant;

/**
 * 手机验证码session的key
 * @author WuZhimin
 *
 */
public enum PhoneCodeEnum {
	
	
	//0注册，1找回密码，2设置/修改支付密码，3验证旧手机，4绑定新手机 5认证店铺 6申请专栏
	TYPE_REGIST(0,"registCode"),
	TYPE_FINDPWD(1,"findPwdCode"),
	TYPE_SET_PAY_PWD(2,"setPayPwd"),
	TYPE_OLD_PHONE(3,"oldPhone"),
	TYPE_NEW_PHONE(4,"newPhone"),
	TYPE_AUTH(5,"auth"),
	ADDFORUM(6,"addForum"),
	;

	private Integer code;
	private String value;

	PhoneCodeEnum() {
	}


	PhoneCodeEnum(Integer code) {
		this.code = code;
	}

	PhoneCodeEnum(String value) {
		this.value = value;
	}

	PhoneCodeEnum(Integer code, String value) {
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
