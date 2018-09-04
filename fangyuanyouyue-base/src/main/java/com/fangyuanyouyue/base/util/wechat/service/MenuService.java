package com.fangyuanyouyue.base.util.wechat.service;
/*package com.csym.yese.wechat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.csym.yese.wechat.resp.Article;
import com.csym.yese.wechat.resp.NewsMessage;
import com.csym.yese.wechat.utils.MessageUtil;

*//**
 * 菜单对应的键
 * @author wuzhimin
 *
 * @date 2014-6-12 下午2:47:20
 *//*
@Service
public class MenuService {
	*//**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 *//*
	
	public String process(Map<String,String> requestMap ,String eventKey) {
		
		// 发送方帐号（open_id）
		String fromUserName = requestMap.get("FromUserName");
		// 公众帐号
		String toUserName = requestMap.get("ToUserName");
		
		String respMessage = "";
		//使用指定大小的图片。第一条图文的图片大小建议为640*320，其他图文的图片大小建议为80*80。如果使用的图片太大，加载慢，而且耗流量；如果使用的图片太小，显示后会被拉伸，失真了很难看。
        // 创建图文消息  
        NewsMessage newsMessage = new NewsMessage();  
        newsMessage.setToUserName(fromUserName);  
        newsMessage.setFromUserName(toUserName);  
        newsMessage.setCreateTime(new Date().getTime());  
        newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
        newsMessage.setFuncFlag(0);  

        List<Article> articleList = new ArrayList<Article>();  
        
        Article article1 = new Article();  
        article1.setTitle("创世易明发展历程。");  
        article1.setDescription("");  
        article1.setPicUrl("http://www.chuangshiyiming.com/img/banner2.jpg");  
        article1.setUrl("http://www.chuangshiyiming.com");  

        Article article2 = new Article();  
        article2.setTitle("专业的定制服务");  
        article2.setDescription("");  
        article2.setPicUrl("http://www.chuangshiyiming.com/img/banner3.jpg");  
        article2.setUrl("http://www.chuangshiyiming.com");  

        Article article3 = new Article();  
        article3.setTitle("用心为客户服务");  
        article3.setDescription("");  
        // 将图片置为空  
        article3.setPicUrl("");  
        article3.setUrl("http://www.chuangshiyiming.com");  

        articleList.add(article1);  
        articleList.add(article2);  
        articleList.add(article3);  
        newsMessage.setArticleCount(articleList.size());  
        newsMessage.setArticles(articleList);  
        respMessage = MessageUtil.newsMessageToXml(newsMessage);  
        
		return respMessage;
	}
}
*/