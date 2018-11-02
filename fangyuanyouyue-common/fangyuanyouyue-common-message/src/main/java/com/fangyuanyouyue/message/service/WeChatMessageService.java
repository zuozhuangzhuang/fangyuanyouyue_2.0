package com.fangyuanyouyue.message.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.message.model.WeChatMessage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public interface WeChatMessageService {
    /**
     * 检验微信传递token真实性
     * @param message
     * @return
     */
    String checkSignature(WeChatMessage message);
}
