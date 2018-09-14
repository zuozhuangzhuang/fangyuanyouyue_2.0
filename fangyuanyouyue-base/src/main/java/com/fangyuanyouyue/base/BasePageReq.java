package com.fangyuanyouyue.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 接口分页请求基类
 * 
 * @author wuzhimin
 * @data 2018年5月14日 下午4:15:47
 */
@Getter
@Setter
@ToString
public class BasePageReq extends BaseReq {

	private static final long serialVersionUID = -4370165388088117479L;



	@ApiModelProperty(name = "id", value = "", dataType = "int",hidden = true)
	private Integer id;

	//起始页
	@ApiModelProperty(name = "start", value = "起始页", dataType = "int",hidden = true)
	private Integer start;

	//每页个数
	@ApiModelProperty(name = "limit", value = "每页个数", dataType = "int",hidden = true)
	private Integer limit;

	//排序规则
	@ApiModelProperty(name = "orders", value = "排序规则", dataType = "String",hidden = true)
	private String orders;

	//排序类型
	@ApiModelProperty(name = "ascType", value = "排序类型 1升序 2降序", dataType = "int",hidden = true)
	private Integer ascType;

	//查询关键词
	@ApiModelProperty(name = "keyword", value = "查询关键词", dataType = "String",hidden = true)
	private String keyword;

	//状态
	@ApiModelProperty(name = "status", value = "状态", dataType = "int",hidden = true)
	private Integer status;

	//开始日期
	@ApiModelProperty(name = "startDate", value = "开始日期", dataType = "String",hidden = true)
	private String startDate;

	//结束日期
	@ApiModelProperty(name = "endDate", value = "结束日期", dataType = "String",hidden = true)
	private String endDate;

}
