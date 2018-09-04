package com.fangyuanyouyue.base.util.wechat.pojo;

/**
 * 按钮的基类
 * 
 * @author wuzhimin 2014-2-27
 * 
 */
public class Button {
	private String name;
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Button [name=" + name + ", url=" + url + "]";
	}

}
