/**
 * Copyright © 2018-2022 平安医疗科技有限公司.All rights reserved.
 */
package com.pamc.redis.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 二次封装实现方法
 * 
 * @author hugh
 * @data 2018年7月7日 上午11:12:19
 */
@Service
public class RedisService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  // 引入 Spring封装好了的RedisTemplate
  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  /**
   * set String方法
   * 
   * @param key
   * @param value
   * @return
   */
  public boolean set(String key, String value, Long expire) {
    boolean flag = false;
    try {
      if(value == null){
        this.redisTemplate.opsForValue().set(key, value);
      }else{
        this.redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
      }
      flag = this.redisTemplate.opsForValue().get(key) == null
          || "".equals(this.redisTemplate.opsForValue().get(key)) ? false : true;
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return flag;
  }

  /**
   * set Object方法
   * 
   * @param key
   * @param obj
   * @return
   */
  public boolean setObject(String key, Object obj) {
    boolean ret = false;
    try {
      this.redisTemplate.opsForValue().set(key, obj);
      ret = this.redisTemplate.opsForValue().get(key) == null
          || "".equals(this.redisTemplate.opsForValue().get(key)) ? false : true;
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return ret;
  }

  /**
   * get Object 方法
   * 
   * @param key
   * @return
   */
  public Object get(String key) {
    Object retsult = null;
    try {
      retsult = this.redisTemplate.opsForValue().get(key);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return retsult;
  }

  /**
   * 设置过期时间
   * 
   * @param key
   * @param expire
   * @return
   */
  public boolean expire(String key, long expire) {
    boolean flag = false;
    try {
      flag = redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return flag;
  }
}
