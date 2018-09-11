package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.wallet.service.impl.SchedualUserServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service",fallback = SchedualUserServiceImpl.class)
@Component
public interface SchedualUserService {
    @RequestMapping(value = "/userFeign/verifyUserById",method = RequestMethod.POST)
    String verifyUserById(@RequestParam(value = "userId") Integer userId);

    @RequestMapping(value = "/userFeign/verifyUserByPhone",method = RequestMethod.POST)
    String verifyUserByPhone(@RequestParam(value = "phone") String phone);

    @RequestMapping(value = "/userFeign/verifyUserByUnionId",method = RequestMethod.POST)
    String verifyUserByUnionId(@RequestParam(value = "unionId") String unionId, @RequestParam(value = "type") Integer type);

    @RequestMapping(value = "/address/getAddressList",method = RequestMethod.POST)
    String getAddressList(@RequestParam(value = "token") String token, @RequestParam(value = "addressId") Integer addressId);

    @RequestMapping(value = "/userFeign/verifyPayPwd",method = RequestMethod.POST)
    String verifyPayPwd(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "payPwd") String payPwd);

}
