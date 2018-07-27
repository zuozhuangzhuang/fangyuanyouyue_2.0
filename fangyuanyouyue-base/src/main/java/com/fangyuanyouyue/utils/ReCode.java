package com.fangyuanyouyue.utils;

/**
 * 返回码
 *
 */
public enum ReCode {
	
	SUCCESS(0),
	FAILD(1),

	ADMIN_NAME_ERROR(2), //用户名不存在
	ADMIN_PWD_ERROR(3),//密码错误
	
	CLIENT_USER_BIND_PHONE(2), //第三方账户登录需要绑定手机号码
	
	LOGIN_STATUS_TIMEOUT(555), //登录状态已失效
	
	
	REPORT_EXISTS(711), //已经举报过该商品
	NICKNAME_RECORD_EXISTS(712), //昵称正在审核中
	SIGN_EXISTS(713)//当天已签到过
	;

	private final Integer value;

	ReCode(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
	

}
