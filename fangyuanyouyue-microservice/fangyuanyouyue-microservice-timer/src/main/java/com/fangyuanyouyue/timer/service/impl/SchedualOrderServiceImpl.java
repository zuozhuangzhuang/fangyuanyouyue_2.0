package com.fangyuanyouyue.timer.service.impl;

import com.fangyuanyouyue.timer.service.SchedualOrderService;
import org.springframework.stereotype.Component;

@Component
public class SchedualOrderServiceImpl implements SchedualOrderService{

    @Override
    public String cancelOrder() {
        return "自动取消订单！";
    }

    @Override
    public String saveReceiptGoods() {
        return "自动收货失败！";
    }
}
