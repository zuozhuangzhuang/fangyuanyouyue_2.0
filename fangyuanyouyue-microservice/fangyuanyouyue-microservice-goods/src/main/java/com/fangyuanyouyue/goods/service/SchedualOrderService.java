package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.goods.service.impl.SchedualOrderServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order-service",fallback = SchedualOrderServiceImpl.class)
@Component
public interface SchedualOrderService {
//    @RequestMapping(value = "/orderFeign/saveOrder",method = RequestMethod.POST)
//    Boolean set(@RequestParam(value = "token") String token, @RequestParam(value = "goodsIds") Integer[] goodsIds);

}
