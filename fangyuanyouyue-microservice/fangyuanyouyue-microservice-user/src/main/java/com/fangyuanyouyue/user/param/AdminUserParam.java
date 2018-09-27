package com.fangyuanyouyue.user.param;

import com.fangyuanyouyue.base.BasePageReq;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
@ApiModel(value = "后台管理用户相关参数")
@Getter
@Setter
@ToString
public class AdminUserParam extends BasePageReq{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1198374192047731064L;

	//用户id
	@ApiModelProperty(name = "userId", value = "用户id", dataType = "int",hidden = true)
	private Integer userId;

	//类型
	@ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
	private Integer type;

	//图片文件
	@ApiModelProperty(name = "imgFile", value = "图片文件", dataType = "file",hidden = true)
	private MultipartFile imgFile;

	//手机号码
	@ApiModelProperty(name = "phone", value = "手机号码", dataType = "String",hidden = true)
	private String phone;

	//状态
	@ApiModelProperty(name = "status", value = "状态", dataType = "int",hidden = true)
	private Integer status;

	//认证状态 1已认证 2未认证
	@ApiModelProperty(name = "authType", value = "认证状态 1已认证 2未认证", dataType = "int",hidden = true)
	private Integer authType;

	//原因
	@ApiModelProperty(name = "content", value = "原因", dataType = "String",hidden = true)
	private String content;

	//修改数量
	@ApiModelProperty(name = "count", value = "修改数量", dataType = "int",hidden = true)
	private Integer count;

}
