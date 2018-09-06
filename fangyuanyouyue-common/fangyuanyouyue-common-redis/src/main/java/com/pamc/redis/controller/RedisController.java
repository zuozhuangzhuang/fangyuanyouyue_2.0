package com.pamc.redis.controller;

import com.fangyuanyouyue.base.BaseController;
import com.fangyuanyouyue.base.BaseResp;
import com.pamc.redis.param.RedisParam;
import com.pamc.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

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
	 * @param str
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
	@PostMapping(value = "get",headers = "Accept=application/json")
	@ResponseBody
	public Object getObject(String key) {
		return redisService.get(key);
	}
	/**
	 * 根据key查询token
	 *
	 * @param key
	 * @return
	 */
	@PostMapping(value = "/getToken")
	@ResponseBody
	public BaseResp getToken(String key) throws IOException {
		return toSuccess(redisService.get(key));
	}
}
