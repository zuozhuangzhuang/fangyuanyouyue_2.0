package com.fangyuanyouyue.message.service.impl;

import com.fangyuanyouyue.message.service.SchedualRedisService;
import org.springframework.stereotype.Component;

@Component
public class SchedualRedisServiceImpl implements SchedualRedisService{

	@Override
	public Boolean set(String key, String value, Long expire) {
		return false;
	}

	@Override
	public Object get(String key) {
		return "系统繁忙";
	}
}
