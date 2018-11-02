package com.fangyuanyouyue.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fangyuanyouyue.base.enums.MiniMsg;
import com.fangyuanyouyue.base.model.Template;
import com.fangyuanyouyue.base.model.TemplateData;
import com.fangyuanyouyue.base.util.wechat.pay.WechatPayConfig;
import com.fangyuanyouyue.base.util.wechat.pojo.AccessToken;
import com.fangyuanyouyue.base.util.wechat.utils.WeixinUtil;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import java.io.IOException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;

public class SendMiniMessage {
    protected static Logger logger = Logger.getLogger(SendMiniMessage.class);

    /**
     * 创建模板消息
     * @param openId
     * @param template_id
     * @param url
     * @param map
     * @return
     */
    public static String makeRouteMessage(String openId, String template_id, String url, Map<String,Object> map, String formId){
        Template template = new Template();
        template.setTouser(openId);
        template.setTemplate_id(template_id);
        template.setUrl(url);
        template.setForm_id(formId);
        Map<String, TemplateData> data = new HashMap<>();
        for(String key:map.keySet()){
            data.put(key,new TemplateData((String)map.get(key),"#173177"));
        }
        template.setData(data);

        String templateJsonStr = JSON.toJSONString(template, SerializerFeature.UseSingleQuotes);
        System.out.println(templateJsonStr);
        return templateJsonStr;
    }

    /**
     * 发送消息
     * @param accessToken
     * @param jsonMsg
     * @return
     */
    public static boolean sendTemplateMessage(String accessToken, String jsonMsg){
        logger.info("消息内容："+jsonMsg);
        boolean result = false;
        //请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        //发送模板消息
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST", jsonMsg);
        if(null != jsonObject){
            int errorCode = jsonObject.getIntValue("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if(0 == errorCode){
                result = true;
                logger.info("模板消息发送成功errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
            }else{
                logger.info("模板消息发送失败errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
            }
        }
        return result;
    }

    /**
     *
     * @param url
     * @param jsonObj
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static JSONObject Post(String url,JSONObject jsonObj) throws ClientProtocolException, IOException{
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonObj.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response=httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(), "UTF-8");
        //输出调用结果
        if(response != null && response.getStatusLine().getStatusCode() == 200) {
            // 生成 JSON 对象
            JSONObject obj = JSONObject.parseObject(result);
            return obj;
        }
        return null;

    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("keyword1","我是评论内容");
        map.put("keyword2","我是评论人");
        map.put("keyword3","我是帖子标题");
        AccessToken accessToken = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
        String message = makeRouteMessage("onuC35VLb3lBsPPKehB3cNxzBU24", MiniMsg.FORUM_COMMENT.getTemplateId(), MiniMsg.FORUM_COMMENT.getPagePath(), map,"eb32f502c72db00580bd721449b715c6");
        boolean flag = sendTemplateMessage(accessToken.getToken(), message);
    }
}
