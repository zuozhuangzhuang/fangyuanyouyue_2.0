package com.fangyuanyouyue.base;

import java.io.Serializable;

/**
 * 接口请求基类
 * 
 * @author wuzhimin
 *
 */
public abstract class BaseReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4314818263498786872L;

	/**
	 * 接口调用平台
	 */
	private String platform;

	/**
	 * APP版本号
	 */
	private String appVersion;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

}
