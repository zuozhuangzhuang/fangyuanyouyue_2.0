package com.fangyuanyouyue.base.util.wechat.req;

/**
 * 图片消息
 * 
 * @author wuzhimin  2014-2-27
 * 
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
