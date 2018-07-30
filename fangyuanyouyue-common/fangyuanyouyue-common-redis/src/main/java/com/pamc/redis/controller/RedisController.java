package com.pamc.redis.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fangyuanyouyue.base.BaseController;
import com.pamc.redis.param.RedisParam;
import com.pamc.redis.service.RedisService;

/**
 * 
 * @author wuzhimin
 *
 */
@RestController
@RequestMapping(value = "/redis")
@RefreshScope
public class RedisController extends BaseController {

	@Resource
	private RedisService redisService;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 保存string
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@PostMapping(value = "/set")
	@ResponseBody
	public Boolean addString(RedisParam str) {
		return redisService.set(str.getKey(), str.getValue(), str.getExpire());
	}

	/**
	 * 根据key查询
	 * 
	 * @param key
	 * @return
	 */
	@GetMapping(value = "get")
	@ResponseBody
	public Object getObject(String key) {
		return redisService.get(key);
	}
}
