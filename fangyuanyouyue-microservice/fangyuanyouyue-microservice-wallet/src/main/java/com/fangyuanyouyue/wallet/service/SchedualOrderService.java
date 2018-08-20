package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.wallet.service.impl.SchedualOrderServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order-service",fallback = SchedualOrderServiceImpl.class)
@Component
public interface SchedualOrderService {

    /**
     * 修改订单状态
     * @param orderNo
     * @param status
     * @return
     */
    @RequestMapping(value = "/orderFeign/updateOrder",method = RequestMethod.POST)
    String updateOrder(@RequestParam(value = "orderNo") String orderNo, @RequestParam(value = "status") Integer status);
}
