package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.goods.service.SchedualUserService;
import org.springframework.stereotype.Component;

@Component
public class SchedualUserServiceImpl implements SchedualUserService{
    @Override
    public String verifyUserById(Integer userId) {
        return "根据用户ID验证用户失败！";
    }

    @Override
    public String verifyUserByPhone(String phone) {
        return "根据手机号验证用户失败！";
    }

    @Override
    public String userIsAuth(Integer userId) {
        return "获取用户是否官方认证失败！";
    }

    @Override
    public String verifyUserByUnionId(String unionId, Integer type) {
        return "根据三方账号失败！";
    }

    @Override
    public String verifyPayPwd(Integer userId, String payPwd) {
        return "验证支付密码失败！";
    }
}
