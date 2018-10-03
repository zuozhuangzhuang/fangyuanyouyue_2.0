package com.fangyuanyouyue.base.enums;

/**
 * 返回码
 *
 */
public enum ReCode {
	
	SUCCESS(0,"操作成功"),
	FAILD(1,"操作失败"),
	IS_MERGE(2,"此手机号已被注册，是否合并账号？"),
	PASSWORD_ERROR(3,"登录密码错误"),
	PAYMENT_PASSWORD_ERROR(4,"支付密码错误"),
	INSUFFICIENT_FUND(5,"余额不足"),
	FILE_IS_NOT_EXCEL(6,"文件不是Excel"),
	DATA_IS_NULL(7,"数据为空，请填写数据"),
	LOGIN_TIME_OUT(10,"登录超时，请重新登录！"),
	FROZEN(11,"您的账号已被冻结，请联系管理员！"),

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
