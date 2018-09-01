package com.fangyuanyouyue.base;

/**
 * 返回值
 * @author wuzhimin
 *
 */
public class BasePageResp extends BaseResp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer total; //总条数
	
	private Integer pageCount;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public String toString() {
		return "BasePageResp [total=" + total + ", pageCount=" + pageCount + ", getCode()=" + getCode()
				+ ", getReport()=" + getReport() + ", getData()=" + getData() + "]";
	}
	
	
}
