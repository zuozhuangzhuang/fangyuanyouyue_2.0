package com.fangyuanyouyue.order.service.impl;

import com.fangyuanyouyue.order.service.SchedualWalletService;
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

}
