package com.fangyuanyouyue.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.AES;
import com.fangyuanyouyue.base.util.SendMiniMessage;
import com.fangyuanyouyue.base.util.wechat.pay.WechatPayConfig;
import com.fangyuanyouyue.base.util.wechat.pojo.AccessToken;
import com.fangyuanyouyue.base.util.wechat.utils.WeixinUtil;
import com.fangyuanyouyue.message.model.WeChatMessage;
import com.fangyuanyouyue.message.service.SchedualRedisService;
import com.fangyuanyouyue.message.service.WeChatMessageService;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

@Service(value = "weChatMessageService")
public class WeChatMessageServiceImpl implements WeChatMessageService{
    @Autowired
    private SchedualRedisService schedualRedisService;

    @Override
    public String checkSignature(WeChatMessage message){
        String signature = message.getSignature();
        String timestamp = message.getTimestamp();
        String nonce = message.getNonce();
        System.out.println(message);

        //将token、timestamp、nonce三个参数进行字典排序
        String[] arr = new String[] {WechatPayConfig.MESSAGE_TOKEN, timestamp, nonce};
        Arrays.sort(arr);

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }

        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) ? message.getEchostr() : "" : "";
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param byteArray
     * @return
     */
    private String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     * @param mByte
     * @return
     */
    private String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

    @Override
    public boolean sendWechatMessage(String miniOpenId, String templateId, String pagePath, Map<String, Object> map, String formId) {
//        try{
//            Object accessTokenRedis = schedualRedisService.get("access_token");
            String accessToken = "";
//            if(accessTokenRedis != null){
//                accessToken = accessTokenRedis.toString();
//            }else{
                AccessToken accessTokenObj = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
                accessToken = accessTokenObj.getToken();
//                schedualRedisService.set("access_token",accessToken,2*60*60L);
//            }
            String message = SendMiniMessage.makeRouteMessage(miniOpenId, templateId, pagePath, map,formId);
            boolean result = SendMiniMessage.sendTemplateMessage(accessToken, message);
            return result;
//        }catch (Exception e){
//            AccessToken accessTokenObj = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
//            schedualRedisService.set("access_token",accessTokenObj.getToken(),2*60*60L);
//            sendWechatMessage(miniOpenId,templateId,pagePath,map,formId);
//        }
//        return false;
    }
}
