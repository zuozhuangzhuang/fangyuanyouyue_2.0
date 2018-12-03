package com.fangyuanyouyue.base.util;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取小程序二维码
 * 参考：https://developers.weixin.qq.com/miniprogram/dev/api/getWXACodeUnlimit.html
 */
public class WXQRCode {
    protected static Logger logger = Logger.getLogger(WXQRCode.class);

    public static String getMiniQr(Integer type,Integer id,String sceneStr, String accessToken) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        String resultStr = null;
        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
            Map<String, Object> param = new HashMap<>();
            param.put("scene", sceneStr);
            param.put("page", "page/center/pages/Orderdetail/Orderdetail");
            param.put("width", 280);
            logger.info("调用生成微信URL接口传参：" + param);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
            logger.info("调用小程序生成微信小程序码URL接口返回结果：" + entity.getBody());

            byte[] result = entity.getBody();
            resultStr = new String(result);


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

    public static void main(String[] args) {
        AccessToken accessToken = WeixinUtil.getAccessToken(WechatPayConfig.APP_ID_MINI, WechatPayConfig.APP_SECRET_MINI);
        String miniQr = getMiniQr(1, 1, "1", accessToken.getToken());
        System.out.println(miniQr);
    }

}
