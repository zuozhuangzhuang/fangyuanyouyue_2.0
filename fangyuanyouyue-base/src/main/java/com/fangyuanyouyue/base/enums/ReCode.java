package com.fangyuanyouyue.base.enums;

/**
 * 返回码
 *
 */
public enum ReCode {
	
	SUCCESS(0,"操作成功"),
	FAILD(1,"操作失败"),

	ADMIN_NAME_ERROR(2,"用户名不存在"), //用户名不存在
	ADMIN_PWD_ERROR(3,"密码错误"),//密码错误
	
	CLIENT_USER_BIND_PHONE(2,"需要绑定手机号码"), //第三方账户登录需要绑定手机号码
	
	LOGIN_STATUS_TIMEOUT(555,"登录已失效，请重新登录"), //登录状态已失效
	
	
	REPORT_EXISTS(711,"您已举报过该商品"), //已经举报过该商品
	NICKNAME_RECORD_EXISTS(712,"昵称正在审核中"), //昵称正在审核中
	SIGN_EXISTS(713,"您已签到")//当天已签到过
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
