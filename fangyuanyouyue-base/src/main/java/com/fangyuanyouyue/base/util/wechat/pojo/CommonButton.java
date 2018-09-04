package com.fangyuanyouyue.base.util.wechat.pojo;


/**
 * 普通按钮（子按钮）
 * 
 * @author wuzhimin  2014-2-27
 * 
 */
public class CommonButton extends Button {
	private String type;
	private String key;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "CommonButton [type=" + type + ", key=" + key + "]";
	}
	
}