package com.fangyuanyouyue.user.service;

import com.fangyuanyouyue.user.service.impl.SchedualOrderServiceImpl;
import com.fangyuanyouyue.user.service.impl.SchedualRedisServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order-service",fallback = SchedualOrderServiceImpl.class)
@Component
public interface SchedualOrderService {
    @RequestMapping(value = "/orderFeign/getProcess",method = RequestMethod.POST)
    String getProcess(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "type") Integer type);

}
