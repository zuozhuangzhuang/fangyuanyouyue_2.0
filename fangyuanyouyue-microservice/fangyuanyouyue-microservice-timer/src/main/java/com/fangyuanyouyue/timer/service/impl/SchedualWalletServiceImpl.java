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
    public String updateAppraisalCount(Integer userId, Integer count,Integer type) {
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

    @Override
    public String addUserBehavior(Integer userId, Integer toUserId, Integer businessId, Integer businessType, Integer type) {
        return "新增用户行为失败！";
    }

    @Override
    public String sendCoupon() {
        return "送优惠券失败！";
    }

    @Override
    public String resetFreeTopCount() {
        return "重置置顶次数失败！";
    }
}
