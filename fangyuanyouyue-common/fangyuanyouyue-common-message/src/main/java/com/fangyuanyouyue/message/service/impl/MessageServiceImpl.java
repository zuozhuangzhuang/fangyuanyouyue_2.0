package com.fangyuanyouyue.message.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.fangyuanyouyue.message.service.MessageService;

@Service(value = "messageService")
public class MessageServiceImpl implements MessageService{
    protected Logger log = Logger.getLogger("smsLog");

    public static String AccessKeyId = "LTAIpIueVqc3Cl2H";
    public static String AccessKeySecret = "9oBv7Hs1K1te1FLoV80r65vkpRl5Ck";

    public static String MODULE_CODE = "SMS_36945084";	//获取验证码模板  您的验证码是${code}，打死不要告诉别人哦！
    public static String MODULE_AUTH_SUCCESS = "SMS_36375219";	//审核成功
    public static String MODULE_AUTH_FAILD = "SMS_36160340";	//审核失败


    IClientProfile profile;
    public static void main(String args[]) throws Exception {
        MessageServiceImpl smm = new MessageServiceImpl();
        smm.sendCode("18103966057","1233","192.168.0.0.1");
//        smm.sendAuthSuccess("18103966057","192.168.0.0.1");
//        smm.sendAuthFail("18103966057","长得太丑啦","192.168.0.0.1");
    }

    public MessageServiceImpl() throws ClientException {
        profile = DefaultProfile.getProfile("cn-hangzhou",
                AccessKeyId, AccessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",
                "sms.aliyuncs.com");
    }

    private void sendMessage(String phone, String param,String ip,String tempCode){
        try {
            IAcsClient client = new DefaultAcsClient(profile);
            SingleSendSmsRequest request = new SingleSendSmsRequest();
            request.setSignName("小方圆");
            request.setTemplateCode(tempCode);
            request.setParamString(param);
            request.setRecNum(phone);
            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            log.info("手机号码："+phone+",参数："+param+",IP："+ip+",tempCode:"+tempCode);
            log.info(httpResponse.toString());
            log.info(httpResponse.getRequestId());
            log.info(httpResponse.getModel());

        } catch (ClientException e) {
            log.error("发送短信失败："+param+",tempCode:"+tempCode);
            e.printStackTrace();
        }
    }

    //发送验证码
    public void sendCode(String phone, String code,String ip) throws Exception{
        sendMessage(phone, "{'code':'"+code+"','product':'小方圆'}", ip, MODULE_CODE);
    }

    //审核成功
    public void sendAuthSuccess(String phone,String ip) throws Exception{
        sendMessage(phone,"{'product':'小方圆'}",ip,MODULE_AUTH_SUCCESS);
    }

    //审核失败
    public void sendAuthFail(String phone,String reason,String ip) throws Exception{
        sendMessage(phone,"{'reason':'"+reason+"','product':'小方圆'}",ip,MODULE_AUTH_FAILD);
    }
}
