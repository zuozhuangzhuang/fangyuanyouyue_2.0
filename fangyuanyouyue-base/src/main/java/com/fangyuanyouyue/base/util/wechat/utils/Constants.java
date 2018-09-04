package com.fangyuanyouyue.base.util.wechat.utils;

/**
 * 一些常量
 * 
 * @author wuzhimin  2014-2-27
 *
 */
public class Constants {

	/**
	 * 公众号的主菜单
	 * 
	 * @return
	 */
	public static String getMainMenu() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好，欢迎关注创世易明微信，请回复数字选择服务：").append("\n\n");
		buffer.append("1 申请账号").append("\n");
		buffer.append("2 体验定位").append("\n");
		buffer.append("3  关于我们").append("\n\n");
		buffer.append("回复“?”显示此帮助菜单");
		return buffer.toString();
	}
	
}
