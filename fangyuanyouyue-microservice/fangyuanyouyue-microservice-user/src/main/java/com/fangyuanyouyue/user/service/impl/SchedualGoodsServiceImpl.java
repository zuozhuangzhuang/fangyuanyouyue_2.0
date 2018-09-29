package com.fangyuanyouyue.user.service.impl;

import com.fangyuanyouyue.user.service.SchedualGoodsService;
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
    public String processTodayGoods() {
        return "失败！";
    }

    @Override
    public String processAllGoods() {
        return "失败";
    }
}
