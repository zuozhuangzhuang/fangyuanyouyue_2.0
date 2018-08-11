package com.fangyuanyouyue.wallet.constant;

/**
 * 手机验证码session的key
 * @author WuZhimin
 *
 */
public enum StatusEnum {
	

	COUPON_NOTUSE(1,"未使用"),
	COUPON_USED(2,"已使用"),
	;

	private Integer code;
	private String value;

	StatusEnum() {
	}


	StatusEnum(Integer code) {
		this.code = code;
	}

	StatusEnum(String value) {
		this.value = value;
	}

	StatusEnum(Integer code, String value) {
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
