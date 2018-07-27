package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.user.service.impl.SchedualGoodsServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-service",fallback = SchedualGoodsServiceImpl.class)
@Component
public interface SchedualGoodsService {
    @RequestMapping(value = "/goods/goodsList",method = RequestMethod.POST)
    String goodsList(@RequestParam(value = "userId") Integer userId,@RequestParam(value = "start") Integer start,@RequestParam(value = "limit") Integer limit);

    @RequestMapping(value = "/goods/goodsInfo",method = RequestMethod.POST)
    String goodsInfo(@RequestParam(value = "goodsId") Integer goodsId);
}
