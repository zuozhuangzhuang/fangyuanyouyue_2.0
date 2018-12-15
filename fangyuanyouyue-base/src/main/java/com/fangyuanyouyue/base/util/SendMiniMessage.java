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
        template.setPage(url);
        template.setForm_id(formId);
        Map<String, TemplateData> data = new HashMap<>();
        for(String key:map.keySet()){
            data.put(key,new TemplateData((String)map.get(key),"#173177"));
        }
        template.setData(data);

        String templateJsonStr = JSONObject.toJSONString(template);
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
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
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

    /**
     * 发送微信客服消息
     * @param accessToken
     * @return
     */
    public static boolean sendCustomerMessage(String accessToken, String jsonMsg){
        logger.info("消息内容："+jsonMsg);
        boolean result = false;
        //请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        //发送模板消息
        JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "POST", jsonMsg);
        if(null != jsonObject){
            int errorCode = jsonObject.getIntValue("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if(0 == errorCode){
                result = true;
                logger.info("客服消息发送成功errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
            }else{
                logger.info("客服消息发送失败errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
            }
        }
        return result;
    }
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("keyword1","我是商品名称");
        map.put("keyword2","我是价格");
        map.put("keyword3","我是购买人姓名");
        map.put("keyword4","2018年11月10日15:50:19");

        AccessToken accessToken = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
        String message = makeRouteMessage("onuC35VLb3lBsPPKehB3cNxzBU24", MiniMsg.GOODS_APPRAISAL_END.getTemplateId(), MiniMsg.GOODS_APPRAISAL_END.getPagePath(), map,"1542359565074");
        //微信模板消息
//        String message = makeRouteMessage("onuC35VLb3lBsPPKehB3cNxzBU24", "7RtiYELicy756YYG5bl2-w_KAau9pgb0-TGuU9EGKEk", "page/tabBar/market/market", map,"wx101549451450129e2031460b3267848754");
        boolean flag = sendTemplateMessage(accessToken.getToken(), message);
        //微信客服消息
//        JSONObject message = new JSONObject();
//        //用户的 OpenID
//        message.put("touser","onuC35VLb3lBsPPKehB3cNxzBU24");
//        //消息类型 text:文本消息 image:图片消息 link:图文链接 miniprogrampage:小程序卡片
//        message.put("msgtype","text");
//        //文本消息内容，msgtype="text" 时必填
//        message.put("content","哈哈哈哈哈哈哈哈");
//        //图片消息，msgtype="image" 时必填
//        message.put("image","");
//        //图片消息，msgtype="link" 时必填
//        message.put("link","");
//        message.put("miniprogrampage","");
//        boolean flag = sendCustomerMessage(accessToken.getToken(), message.toJSONString());
//        boolean flag = sendCustomerMessage("15_Jtfz1LXay3JP7RyMsuHsIaKSjz948Ly2jTlWXBIr2s5usCGpXdpu88Ov18wnwoIhaTZhnn_d0LdGGOs4lS4mo_l440AU_enJ4K6h9sZkr467mKmA16ZOAERGVtQSWPfAFAWAT", message.toJSONString());
//        String token = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI).getToken();
//        System.out.println(token);
//        String a = HttpUtil.sendGet("https://api.weixin.qq.com/cgi-bin/customservice/getkflist", "access_token=15_iJihcUl7xwAUyUUacCuLTXH-UOjgx6UFHva_utRNTH942MeNoLS53WI2xqAx5Q-5g2QUy1C18CfGZthZtA9jl5olrdA-ziMKQJwYBkzUk09Te_BRsiOYDCIgfP4ey35z7BVfSv7yw2qsYgSxJFMaAFAUQX");
        /*
参数	            说明
kf_account	        完整客服帐号，格式为：帐号前缀@公众号微信号
kf_nick	            客服昵称
kf_id	            客服编号
kf_headimgurl	    客服头像
kf_wx	            如果客服帐号已绑定了客服人员微信号， 则此处显示微信号
invite_wx	        如果客服帐号尚未绑定微信号，但是已经发起了一个绑定邀请， 则此处显示绑定邀请的微信号
invite_expire_time	如果客服帐号尚未绑定微信号，但是已经发起过一个绑定邀请， 邀请的过期时间，为unix 时间戳
invite_status	    邀请的状态，有等待确认“waiting”，被拒绝“rejected”， 过期“expired”
         */
//        System.out.println(a);

//        String b = HttpUtil.sendGet("https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist", "access_token=15_iJihcUl7xwAUyUUacCuLTXH-UOjgx6UFHva_utRNTH942MeNoLS53WI2xqAx5Q-5g2QUy1C18CfGZthZtA9jl5olrdA-ziMKQJwYBkzUk09Te_BRsiOYDCIgfP4ey35z7BVfSv7yw2qsYgSxJFMaAFAUQX");
        /*
参数	            说明
kf_account	        完整客服帐号，格式为：帐号前缀@公众号微信号
status	            客服在线状态，目前为：1、web 在线
kf_id	            客服编号
accepted_case	    客服当前正在接待的会话数
         */
//        System.out.println(b);

    }
}
