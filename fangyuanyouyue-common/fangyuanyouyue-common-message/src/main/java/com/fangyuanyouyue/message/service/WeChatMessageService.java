package com.fangyuanyouyue.message.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.message.model.WeChatMessage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

public interface WeChatMessageService {
    /**
     * 检验微信传递token真实性
     * @param message
     * @return
     */
    String checkSignature(WeChatMessage message);

    /**
     * 发送微信模板消息
     * @param miniOpenId
     * @param templateId
     * @param pagePath
     * @param map
     * @param formId
     * @return
     */
    boolean sendWechatMessage(String miniOpenId, String templateId, String pagePath, Map<String,Object> map,String formId);
}
