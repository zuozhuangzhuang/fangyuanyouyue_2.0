package com.fangyuanyouyue.goods.service.impl;

import com.fangyuanyouyue.goods.service.SchedualOrderService;
import org.springframework.stereotype.Component;

@Component
public class SchedualOrderServiceImpl implements SchedualOrderService{
    @Override
    public String getOrderStatus(Integer userId, Integer orderId) {
        return "获取订单状态失败！";
    }
}
