package com.fangyuanyouyue.forum.service.impl;

import org.springframework.stereotype.Component;

import com.fangyuanyouyue.forum.service.SchedualRedisService;

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
