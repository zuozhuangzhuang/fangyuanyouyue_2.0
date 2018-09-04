package com.fangyuanyouyue.base.util.wechat.pojo;

import java.util.ArrayList;
import java.util.List;


/**
 * 复杂按钮（父按钮）
 * 
 * @author wuzhimin  2014-2-27
 * 
 */
public class ComplexButton extends Button {
	private List<Button> sub_button = new ArrayList<Button>();
	private String type;
	private String key;
	
	public void add(Button button){
		sub_button.add(button);
	}

	public List<Button> getSub_button() {
		return sub_button;
	}

	public void setSub_button(List<Button> sub_button) {
		this.sub_button = sub_button;
	}

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
		return "ComplexButton [sub_button=" + sub_button + ", type=" + type
				+ ", key=" + key + "]";
	}

	
}