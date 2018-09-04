package com.fangyuanyouyue.base.util.wechat.service;
/*package com.csym.yese.wechat.service;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csym.yese.model.WxMsg;
import com.csym.yese.service.CommonService;
import com.csym.yese.system.Page;
import com.csym.yese.wechat.resp.TextMessage;
import com.csym.yese.wechat.utils.MessageUtil;

*//**
 * 处理消息
 * @author wuzhimin
 *
 * @date 2014-6-19 上午9:55:22
 *//*
@Service
public class TextService {

	
	@Resource
	private CommonService commonService;
	
	*//**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 *//*
	
	public String processRequest(Map<String,String> requestMap) {
		
		String respMessage = null;
		
		try {
			// 默认返回的文本消息内容
			String respContent = "你说的我还不明白！";

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			
			//接收到的文本
			String content = requestMap.get("Content"); 
			
			Page page = new Page(0,1);
			System.out.println("开始查找数据");
			//1、查找一样的
			commonService.findByProperty(WxMsg.class,page, "key", content,"time desc ");
			if(page.getRoot()!=null&&page.getRoot().size()>0){
				WxMsg msg = (WxMsg) page.getRoot().get(0);
				respContent = msg.getMsg();
			}else{
				//2、查找相似的
				Page page_ = new Page(0,1);
				commonService.findLikeProperty(WxMsg.class,page_, "key", content,"time desc ");

				if(page_.getRoot()!=null&&page_.getRoot().size()>0){
					WxMsg msg = (WxMsg) page_.getRoot().get(0);
					respContent = msg.getMsg();
				}
			}
			
			
			System.out.println("查找数据结束");
			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);


			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}
}
*/