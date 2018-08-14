package com.fangyuanyouyue.order.service;

import com.fangyuanyouyue.order.service.impl.SchedualMessageServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "message-service",fallback = SchedualMessageServiceImpl.class)
@Component
public interface SchedualMessageService {
    @RequestMapping(value = "/message/sendCode",method = RequestMethod.POST)
    String sendCode(@RequestParam(value = "phone") String phone, @RequestParam(value = "type") Integer type);
    
    @RequestMapping(value = "/message/easemob/regist",method = RequestMethod.POST)
    String easemobRegist(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password);

    @RequestMapping(value = "/message/easemob/message",method = RequestMethod.POST)
    String easemobMessage(@RequestParam(value = "userName") String userName, @RequestParam(value = "content") String content,
                          @RequestParam(value = "type") String type, @RequestParam(value = "businessId") String businessId);
    
}
