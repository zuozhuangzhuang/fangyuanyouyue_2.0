package com.fangyuanyouyue.wallet.constant;

/**
 * 手机验证码session的key
 * @author WuZhimin
 *
 */
public enum TypeEnum {
	

	VRITUAL_GOODS(1,"虚拟商品"),
	ENTITY_GOODS(2,"实物商品"),
	;

	private Integer code;
	private String value;

	TypeEnum() {
	}


	TypeEnum(Integer code) {
		this.code = code;
	}

	TypeEnum(String value) {
		this.value = value;
	}

	TypeEnum(Integer code, String value) {
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
