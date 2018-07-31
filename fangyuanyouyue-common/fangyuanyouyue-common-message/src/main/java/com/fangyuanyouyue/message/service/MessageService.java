package com.fangyuanyouyue.message.service;

public interface MessageService {
    //发送验证码
    public void sendCode(String phone, String code,String ip) throws Exception;
}
