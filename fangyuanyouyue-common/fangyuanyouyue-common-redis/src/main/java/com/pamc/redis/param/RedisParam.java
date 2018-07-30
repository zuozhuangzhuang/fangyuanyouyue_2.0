package com.pamc.redis.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Redis参数")
public class RedisParam {
	// 公用
	@ApiModelProperty(name = "key", value = "Key", dataType = "String", hidden = true)
	private String key; // 起始页

	@ApiModelProperty(name = "value", value = "Value", dataType = "String", hidden = true)
	private String value; // 限制页

	@ApiModelProperty(name = "expire", value = "Expire", dataType = "Long", hidden = true)
	private Long expire; // 时间

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}
	

}
