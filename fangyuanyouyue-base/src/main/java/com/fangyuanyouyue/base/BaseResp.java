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
	private Integer code;

	// 结果原因
	private String report;

	// 数据
	private Object data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BaseResp{" +
				"code=" + code +
				", report='" + report + '\'' +
				", data=" + data +
				'}';
	}
}
