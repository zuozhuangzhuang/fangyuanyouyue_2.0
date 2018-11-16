package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.goods.service.impl.SchedualUserServiceImpl;
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

    @RequestMapping(value = "/userFeign/userIsAuth",method = RequestMethod.POST)
    String userIsAuth(@RequestParam(value = "userId") Integer userId);

    @RequestMapping(value = "/userFeign/verifyPayPwd",method = RequestMethod.POST)
    String verifyPayPwd(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "payPwd") String payPwd);

    @RequestMapping(value = "/userFeign/isAuth",method = RequestMethod.POST)
    String isAuth(@RequestParam(value = "userId") Integer userId);

    /**
     * 获取用户formId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userFeign/getFormId",method = RequestMethod.POST)
    String getFormId(@RequestParam(value = "userId") Integer userId);

    /**
     * 获取用户openId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userFeign/getOpenId",method = RequestMethod.POST)
    String getOpenId(@RequestParam(value = "userId") Integer userId);

}
