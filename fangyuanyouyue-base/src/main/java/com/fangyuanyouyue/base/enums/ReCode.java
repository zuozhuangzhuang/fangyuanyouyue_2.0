package com.fangyuanyouyue.base.enums;

/**
 * 返回码
 *
 */
public enum ReCode {
	
	SUCCESS(0,"操作成功"),
	FAILD(1,"操作失败"),

	;

	private final Integer value;
	
	private final String message;

	ReCode(Integer value,String message) {
		this.value = value;
		this.message = message;
	}

	public Integer getValue() {
		return value;
	}
	
	public String getMessage() {
		return message;
	}
	

}
