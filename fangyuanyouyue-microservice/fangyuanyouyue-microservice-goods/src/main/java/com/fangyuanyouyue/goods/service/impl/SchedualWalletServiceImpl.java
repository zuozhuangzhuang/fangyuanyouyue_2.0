package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.goods.service.SchedualWalletService;
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
    public String orderPayByWechat(String orderNo, BigDecimal price,String notifyUrl) {
        return "微信支付失败！";
    }

    @Override
    public String orderPayByALi(String orderNo, BigDecimal price,String notifyUrl) {
        return "支付宝支付失败！";
    }

    @Override
    public String addUserBalanceDetail(Integer userId, BigDecimal amount, Integer payType, Integer type, String orderNo, String title, Integer sellerId, Integer buyerId, Integer orderType) {
        return "新增用户收支信息失败！";
    }

    @Override
    public String orderPayByWechatMini(Integer userId, String orderNo, BigDecimal price, String notifyUrl) {
        return "小程序支付失败！";
    }
}
