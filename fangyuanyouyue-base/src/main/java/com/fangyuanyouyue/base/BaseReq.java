package com.fangyuanyouyue.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 接口请求基类
 * 
 * @author wuzhimin
 *
 */
@Getter
@Setter
@ToString
public abstract class BaseReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4314818263498786872L;

	/**
	 * 接口调用平台
	 */
	@ApiModelProperty(name = "platform", value = "接口调用平台", dataType = "String",hidden = true)
	private String platform;

	/**
	 * APP版本号
	 */
	@ApiModelProperty(name = "appVersion", value = "APP版本号", dataType = "String",hidden = true)
	private String appVersion;

}
