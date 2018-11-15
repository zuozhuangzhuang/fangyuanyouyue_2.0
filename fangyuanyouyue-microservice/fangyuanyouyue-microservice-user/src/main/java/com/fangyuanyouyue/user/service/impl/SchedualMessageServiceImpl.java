package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.user.service.SchedualMessageService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SchedualMessageServiceImpl implements SchedualMessageService {

    @Override
    public String sendCode(String phone, Integer typet) {
        return "发送验证码失败！";
    }

	@Override
	public String easemobRegist(String userName, String password) {
        return "环信注册失败！";
	}

	@Override
	public String easemobMessage(String userName, String content, String type,String jumpType, String businessId) {
        return "发送环信消息失败！";
	}

    @Override
    public String wechatMessage(String miniOpenId,String templateId,
                                String pagePath,Map<String,Object> map,
                                String formId) {
        return "发送微信模板消息失败！";
    }
}
