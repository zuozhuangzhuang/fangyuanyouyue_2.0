package com.fangyuanyouyue.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fangyuanyouyue.order.service.impl.SchedualRedisServiceImpl;

@FeignClient(value = "redis-service",fallback = SchedualRedisServiceImpl.class)
@Component
public interface SchedualRedisService {
    @RequestMapping(value = "/redis/set",method = RequestMethod.POST)
    Boolean set(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value, @RequestParam(value = "expire") Long expire);

    @RequestMapping(value = "/redis/get",method = RequestMethod.POST)
    Object get(@RequestParam(value = "key") String key);
}
