package com.fangyuanyouyue.user.constant;

/**
 * 手机验证码session的key
 * @author WuZhimin
 *
 */
public enum StatusEnum {

	YES(1),
	NO(2),
	
	ADDRESS_DEFAULT(1),
	ADDRESS_OTHER(2),
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
