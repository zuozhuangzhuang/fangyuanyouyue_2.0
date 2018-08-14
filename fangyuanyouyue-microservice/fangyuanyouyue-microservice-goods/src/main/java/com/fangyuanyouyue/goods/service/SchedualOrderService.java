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
    @RequestMapping(value = "/orderFeign/getOrderStatus",method = RequestMethod.POST)
    String getOrderStatus(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "orderId") Integer orderId);

}
