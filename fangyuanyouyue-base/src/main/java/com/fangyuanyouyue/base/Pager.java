package com.fangyuanyouyue.base;

import java.util.List;

/**
 * 返回值
 * @author wuzhimin
 *
 */
public class Pager  {


	private Integer total; //总条数
	
	private List datas;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List getDatas() {
		return datas;
	}

	public void setDatas(List datas) {
		this.datas = datas;
	}

	
}
