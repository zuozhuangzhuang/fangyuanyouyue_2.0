package com.fangyuanyouyue.base.util.wechat.service;
/*package com.csym.yese.wechat.service;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.csym.yese.wechat.resp.TextMessage;
import com.csym.yese.wechat.utils.Constants;
import com.csym.yese.wechat.utils.MessageUtil;

*//**
 * 微信核心服务类
 * 
 * @author wuzhimin  2014-2-27
 * 
 *//*
@Service("coreService")
public class CoreService {
	
	@Resource
	private MenuService menuService;
	@Resource
	private TextService textService;
	
	*//**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 *//*
	public String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {

			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);

			// 默认返回的文本消息内容
			String respContent = "返回消息。";
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

               respMessage = textService.processRequest(requestMap);
				
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "有趣的图片！";

   				textMessage.setContent(respContent);
   				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "原来你在这里啊！";

   				textMessage.setContent(respContent);
   				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "这是什么链接呢！";

   				textMessage.setContent(respContent);
   				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "你说什么？大声点！";

   				textMessage.setContent(respContent);
   				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
                
				
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "谢谢您的关注！"+ "\n"+Constants.getMainMenu();;
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					//  取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {

					 // 事件KEY值，与创建自定义菜单时指定的KEY值对应  
                    String eventKey = requestMap.get("EventKey"); 
                    respMessage = "菜单功能正在完善，请稍候再试！"; 
                    //处理菜单点击事件
                    respMessage = menuService.process(requestMap, eventKey);
                    
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}
}
*/