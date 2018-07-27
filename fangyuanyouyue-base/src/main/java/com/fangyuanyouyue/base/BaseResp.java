package com.fangyuanyouyue.base;

import java.io.Serializable;

/**
 * 返回值
 * @author wuzhimin
 *
 */
public class BaseResp implements Serializable {

	/**
	 *  
	 */
	private static final long serialVersionUID = 2247892260490038849L;

	// 状态码
	private Integer reCode;

	// 结果原因
	private String reMsg;

	// 数据
	private Object data;

	public Integer getReCode() {
		return reCode;
	}

	public void setReCode(Integer reCode) {
		this.reCode = reCode;
	}

	public String getReMsg() {
		return reMsg;
	}

	public void setReMsg(String reMsg) {
		this.reMsg = reMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
