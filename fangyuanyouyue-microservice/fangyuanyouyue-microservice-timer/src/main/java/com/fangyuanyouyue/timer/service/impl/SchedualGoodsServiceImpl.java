package com.fangyuanyouyue.timer.service.impl;

import com.fangyuanyouyue.timer.service.SchedualGoodsService;
import org.springframework.stereotype.Component;

@Component
public class SchedualGoodsServiceImpl implements SchedualGoodsService{
    @Override
    public String goodsList(Integer userId, Integer start, Integer limit) {
        return "获取商品列表失败！";
    }

    @Override
    public String goodsInfo(Integer goodsId) {
        return "获取商品详情失败！";
    }

    @Override
    public String getProcess(Integer key) {
        return "获取统计数据失败！";
    }

    @Override
    public String depreciate() {
        return "抢购降价失败！";
    }

    @Override
    public String refuseBargain() {
        return "自动处理议价失败！";
    }
}
