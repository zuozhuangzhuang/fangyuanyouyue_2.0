package com.fangyuanyouyue.base.util;

import com.fangyuanyouyue.base.enums.MiniPage;
import com.fangyuanyouyue.base.util.wechat.pay.WechatPayConfig;
import com.fangyuanyouyue.base.util.wechat.pojo.AccessToken;
import com.fangyuanyouyue.base.util.wechat.utils.WeixinUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取小程序二维码
 * 参考：https://developers.weixin.qq.com/miniprogram/dev/api/getWXACodeUnlimit.html
 */
public class WXQRCode {
    protected static Logger logger = Logger.getLogger(WXQRCode.class);

    public static String getMiniQrBase64(String sceneStr, String accessToken,String page) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        String resultStr = null;
        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
            Map<String, Object> param = new HashMap<>();
            param.put("scene", sceneStr);
//            param.put("page", page);
            param.put("width", 280);
            logger.info("调用生成微信URL接口传参：" + param);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class);

            byte[] result = entity.getBody();
            resultStr = "data:image/png;base64,"+Base64.encodeBase64String(result);
//            logger.info("调用小程序生成微信小程序码URL接口返回结果：" + resultStr);

        } catch (Exception e) {
            logger.error("调用小程序生成微信小程序码URL接口异常", e);

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultStr;
    }

    public static InputStream getMiniQrInput(String sceneStr, String accessToken,String page) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        String resultStr = null;
        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
            Map<String, Object> param = new HashMap<>();
            param.put("scene", sceneStr);
            param.put("page", page);
            param.put("width", 280);
            logger.info("调用生成微信URL接口传参：" + param);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class);

            byte[] result = entity.getBody();
            resultStr = "data:image/png;base64,"+Base64.encodeBase64String(result);
//            logger.info("调用小程序生成微信小程序码URL接口返回结果：" + resultStr);
            inputStream = new ByteArrayInputStream(result);

        } catch (Exception e) {
            logger.error("调用小程序生成微信小程序码URL接口异常", e);

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return inputStream;
    }

    public static void main(String[] args) {
        AccessToken accessToken = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
        String miniQr = getMiniQrBase64("userId=106418#inviteCode=abcdefgh", accessToken.getToken(), MiniPage.SHOP_DETAIL.getUrl());
    }

}
