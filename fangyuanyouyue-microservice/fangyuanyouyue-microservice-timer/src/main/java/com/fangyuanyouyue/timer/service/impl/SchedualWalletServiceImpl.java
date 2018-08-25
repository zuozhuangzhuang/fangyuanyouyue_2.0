package com.fangyuanyouyue.timer.service.impl;

import com.fangyuanyouyue.timer.service.SchedualWalletService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SchedualWalletServiceImpl implements SchedualWalletService{
    @Override
    public String updateBalance(Integer userId, BigDecimal amount,Integer type) {
        return "修改余额失败！";
    }


    @Override
    public String getAppraisalCount(Integer userId) {
        return "获取免费鉴定次数失败";
    }

    @Override
    public String updateAppraisalCount(Integer userId, Integer count) {
        return "修改免费鉴定次数失败！";
    }

    @Override
    public String updateLevel() {
        return "定时更新用户等级";
    }

    @Override
    public String cancelVip() {
        return "会员自动到期";
    }
}
