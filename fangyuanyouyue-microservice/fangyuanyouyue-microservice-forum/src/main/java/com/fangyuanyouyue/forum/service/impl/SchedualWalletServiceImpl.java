package com.fangyuanyouyue.forum.service.impl;

import com.fangyuanyouyue.forum.service.SchedualWalletService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SchedualWalletServiceImpl implements SchedualWalletService{
    @Override
    public String updateBalance(Integer userId, BigDecimal amount,Integer type) {
        return "修改余额失败！";
    }

    @Override
    public String updateScore(Integer userId, Long score,Integer type) {
        return "修改积分失败！";
    }

    @Override
    public String updateCredit(Integer userId, Long credit, Integer type) {
        return "修改信誉度失败！";
    }

    @Override
    public String orderPayByWechat(String orderNo, BigDecimal price,String notifyUrl) {
        return "微信支付失败！";
    }
    @Override
    public String orderPayByALi(String orderNo, BigDecimal price,String notifyUrl) {
        return "支付宝支付失败！";
    }
}
