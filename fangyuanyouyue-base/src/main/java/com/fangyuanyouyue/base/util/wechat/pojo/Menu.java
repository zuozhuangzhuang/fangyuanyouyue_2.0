package com.fangyuanyouyue.base.util.wechat.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 * 
 * @author wuzhimin 2014-2-27
 * 
 */
public class Menu {

	private List<Button> button = new ArrayList<Button>();

	public void add(Button b) {
		button.add(b);
	}

	public List<Button> getButton() {
		return button;
	}

	public void setButton(List<Button> button) {
		this.button = button;
	}

	@Override
	public String toString() {
		return "Menu [buttons=" + button + "]";
	}

}