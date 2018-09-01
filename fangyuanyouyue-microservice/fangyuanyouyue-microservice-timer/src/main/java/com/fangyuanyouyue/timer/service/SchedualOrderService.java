package com.fangyuanyouyue.timer.service;

import com.fangyuanyouyue.timer.service.impl.SchedualOrderServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order-service",fallback = SchedualOrderServiceImpl.class)
@Component
public interface SchedualOrderService {


    /**
     * 自动取消订单
     * @return
     */
    @RequestMapping(value = "/timer/cancelOrder",method = RequestMethod.POST)
    String cancelOrder();

    /**
     * 自动收货
     * @return
     */
    @RequestMapping(value = "/timer/saveReceiptGoods",method = RequestMethod.POST)
    String saveReceiptGoods();

    /**
     * 自动处理退货
     * @return
     */
    @RequestMapping(value = "/timer/updateOrderRefund",method = RequestMethod.POST)
    String updateOrderRefund();

}
