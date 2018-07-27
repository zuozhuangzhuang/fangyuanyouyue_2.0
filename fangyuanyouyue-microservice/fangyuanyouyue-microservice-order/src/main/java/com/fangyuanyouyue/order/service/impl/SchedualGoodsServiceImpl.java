package com.fangyuanyouyue.order.service.impl;

import com.fangyuanyouyue.order.service.SchedualGoodsService;
import org.springframework.stereotype.Component;

@Component
public class SchedualGoodsServiceImpl implements SchedualGoodsService{
    @Override
    public String goodsInfo(Integer goodsId) {
        return "系统繁忙，请稍后重试！";
    }

    @Override
    public String updateGoodsStatus(Integer goodsId,Integer status) {
        return "系统繁忙，请稍后重试！";
    }

    @Override
    public String goodsMainImg(Integer goodsId) {
        return "系统繁忙，请稍后重试！";
    }
}
