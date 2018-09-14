package com.fangyuanyouyue.user.constant;

/**
 * 手机验证码session的key
 * @author WuZhimin
 *
 */
public enum StatusEnum {

	YES(1),NO(2),

	//收货地址类型 1默认地址 2普通地址
	ADDRESS_DEFAULT(1),ADDRESS_OTHER(2),
	
	//实名认证状态 1申请 2通过 3未通过
	AUTH_APPLY(1),AUTH_ACCEPT(2),AUTH_REJECT(3),

	//认证店铺状态 1申请 2认证 3未认证
	AUTH_TYPE_APPLY(1),AUTH_TYPE_ACCEPT(2),AUTH_TYPE_REJECT(3),

	//状态 1待支付 2已完成 3已删除
	ORDER_UNPAID(1),ORDER_COMPLETE(2),ORDER_DELETE(3),

	//类型 1微信 2QQ 3微博
	WECHAR(1),QQ(2),WEIBO(3)

	//注册来源 1app 2微信小程序

	//注册平台 1安卓 2IOS 3小程序

	//状态 1正常 2冻结 3删除

	//性别，1男 2女 0不确定

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
