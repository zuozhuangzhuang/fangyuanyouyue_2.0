package com.fangyuanyouyue.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 返回值
 * @author wuzhimin
 *
 */
@Getter
@Setter
@ToString
public class BaseResp implements Serializable {

	/**
	 *  
	 */
	private static final long serialVersionUID = 2247892260490038849L;

	// 状态码
	private Integer code;

	// 结果原因
	private String report;

	// 数据
	private Object data;

}
