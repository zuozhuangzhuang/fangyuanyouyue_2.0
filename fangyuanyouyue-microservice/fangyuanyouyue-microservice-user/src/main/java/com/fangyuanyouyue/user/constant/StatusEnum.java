package com.fangyuanyouyue.user.constant;

/**
 * 手机验证码session的key
 * @author WuZhimin
 *
 */
public enum StatusEnum {
	
	
	ADDRESS_DEFAULT(0),
	ADDRESS_OTHER(1),
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
