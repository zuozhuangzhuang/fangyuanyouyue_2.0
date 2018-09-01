package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.wallet.service.impl.SchedualMessageServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "message-service",fallback = SchedualMessageServiceImpl.class)
@Component
public interface SchedualMessageService {
    /**
     * 发送验证码
     * @param phone
     * @param type
     * @return
     */
    @RequestMapping(value = "/message/sendCode",method = RequestMethod.POST)
    String sendCode(@RequestParam(value = "phone") String phone, @RequestParam(value = "type") Integer type);

    /**
     * 环信注册
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/message/easemob/regist",method = RequestMethod.POST)
    String easemobRegist(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password);

    /**
     * 发送环信信息
     * @param userName
     * @param content
     * @param type  消息类型 1系统消息 2交易消息 3社交消息 4新增粉丝 5邀请我
     * @param jumpType  跳转类型 1系统消息 2商品消息 3订单消息 4视频消息 5帖子消息 6专栏消息 7全民鉴定消息 8商品、抢购评论消息 9帖子评论消息 10视频评论消息 11全民鉴定评论消息 12会员特权 13钱包余额
     * @param businessId
     * @return
     */
    @RequestMapping(value = "/message/easemob/message",method = RequestMethod.POST)
    String easemobMessage(@RequestParam(value = "userName") String userName, @RequestParam(value = "content") String content,
                          @RequestParam(value = "type") String type,@RequestParam(value = "jumpType") String jumpType, @RequestParam(value = "businessId") String businessId);

    /**
     * 发送微信模版消息
     * @param userName
     * @param content
     * @param type  扩展消息类型 1系统消息 2商品消息 3订单消息 4视频消息 5帖子消息 6专栏消息 7全民鉴定消息 8商品、抢购评论消息 9帖子评论消息 10视频评论消息 11全民鉴定评论消息 12会员特权 13钱包余额
     * @param businessId
     * @return
     */
    @RequestMapping(value = "/message/wechat/message",method = RequestMethod.POST)
    String wechatMessage(@RequestParam(value = "userName") String userName, @RequestParam(value = "content") String content,
                         @RequestParam(value = "type") String type, @RequestParam(value = "businessId") String businessId);

}
