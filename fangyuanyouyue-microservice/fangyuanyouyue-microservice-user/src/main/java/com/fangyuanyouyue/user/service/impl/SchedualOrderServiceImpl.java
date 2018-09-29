package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.user.service.SchedualOrderService;
import org.springframework.stereotype.Component;

@Component
public class SchedualOrderServiceImpl implements SchedualOrderService{
    @Override
    public String getProcess(Integer key, Integer type) {
        return "获取统计数据失败！";
    }


    @Override
    public String processTodayOrder() {
        return "每小时统计一次今日订单失败！";
    }

    @Override
    public String processAllOrder() {
        return "每小时统计一次总订单失败！";
    }

}
