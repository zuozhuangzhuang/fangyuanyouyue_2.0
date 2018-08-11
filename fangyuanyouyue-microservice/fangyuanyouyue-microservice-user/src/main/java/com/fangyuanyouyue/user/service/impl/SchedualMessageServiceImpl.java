package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.user.service.SchedualMessageService;
import org.springframework.stereotype.Component;

@Component
public class SchedualMessageServiceImpl implements SchedualMessageService {

    @Override
    public String sendCode(String phone, Integer typet) {
        return "发送验证码失败！";
    }
}
