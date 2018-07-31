package com.fangyuanyouyue.goods.service;

import com.fangyuanyouyue.goods.service.impl.SchedualRedisServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "redis-service",fallback = SchedualRedisServiceImpl.class)
@Component
public interface SchedualRedisService {
    @RequestMapping(value = "/redis/set",method = RequestMethod.POST)
    Boolean set(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value, @RequestParam(value = "expire") Long expire);

    @RequestMapping(value = "/redis/get",method = RequestMethod.POST)
    Object get(@RequestParam(value = "key") String key);
}
