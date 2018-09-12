package com.fangyuanyouyue.user.param;

import com.fangyuanyouyue.base.BasePageReq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class AdminUserParam extends BasePageReq{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1198374192047731064L;

	//用户id
	private Integer userId;

	//类型
	private Integer type;

	//图片文件
	private MultipartFile imgFile;

	//手机号码
	private String phone;

	//状态
	private Integer status;

	//认证状态 1已认证 2未认证
	private Integer authType;
}
