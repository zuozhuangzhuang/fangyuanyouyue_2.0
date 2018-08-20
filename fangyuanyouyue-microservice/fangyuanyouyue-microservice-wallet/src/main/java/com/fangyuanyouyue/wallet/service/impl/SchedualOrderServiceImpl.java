package com.fangyuanyouyue.wallet.service.impl;

import com.fangyuanyouyue.wallet.service.SchedualOrderService;
import org.springframework.stereotype.Component;

@Component
public class SchedualOrderServiceImpl implements SchedualOrderService{
    @Override
    public String updateOrder(String orderNo, Integer status) {
        return "修改订单状态失败！";
    }
}
