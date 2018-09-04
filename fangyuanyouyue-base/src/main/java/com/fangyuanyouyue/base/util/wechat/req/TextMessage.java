package com.fangyuanyouyue.base.util.wechat.req;

/**
 * 文本消息
 * 
 * @author wuzhimin  2014-2-27
 * 
 */
public class TextMessage extends BaseMessage {
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}