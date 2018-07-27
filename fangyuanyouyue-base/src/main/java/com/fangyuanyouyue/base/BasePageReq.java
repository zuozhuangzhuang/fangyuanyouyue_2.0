package com.fangyuanyouyue.base;

/**
 * 接口分页请求基类
 * 
 * @author hugh
 * @data 2018年5月14日 下午4:15:47
 */
public abstract class BasePageReq extends BaseReq {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4370165388088117479L;

	/**
	 * 第几条开始
	 */
	private int start = 1;

	/**
	 * 每页多少条
	 */
	private int limit = 20;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
