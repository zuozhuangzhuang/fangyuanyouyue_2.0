package com.fangyuanyouyue.forum.param;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "鉴定相关参数")
@Getter
@Setter
@ToString
public class AppraisalParam{
	//公用
	@ApiModelProperty(name = "start", value = "起始页", dataType = "int",hidden = true)
	private Integer start; // 起始页

	@ApiModelProperty(name = "limit", value = "限制页", dataType = "int",hidden = true)
	private Integer limit; // 限制页

	@ApiModelProperty(name = "token", value = "用户token", dataType = "String",hidden = true)
	private String token;//用户token

	@ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
	private Integer type;//类型

	//TODO 需要在API网关通过授权才能获取到userId
	@ApiModelProperty(name = "userId", value = "用户id", dataType = "int",hidden = true)
	private Integer userId;//用户id
	
	@ApiModelProperty(name = "appraisalId", value = "全民鉴定id", dataType = "int",hidden = true)
	private Integer appraisalId;//鉴定id

	@ApiModelProperty(name = "viewpoint", value = "看法，1看真 2看假", dataType = "int",hidden = true)
	private Integer viewpoint;//看法，1看真 2看假

	@ApiModelProperty(name = "commentId", value = "评论id", dataType = "int",hidden = true)
	private Integer commentId;//评论id

	@ApiModelProperty(name = "keyword", value = "关键字搜索", dataType = "String",hidden = true)
	private String keyword;//帖子内容

	@ApiModelProperty(name = "bonus", value = "鉴定赏金", dataType = "BigDecimal",hidden = true)
	private BigDecimal bonus;//鉴定赏金

	@ApiModelProperty(name = "title", value = "标题", dataType = "String",hidden = true)
	private String title;//标题

	@ApiModelProperty(name = "content", value = "商品描述", dataType = "String",hidden = true)
	private String content;//商品描述

	@ApiModelProperty(name = "imgUrls", value = "图片数组", dataType = "String",hidden = true)
	private String[] imgUrls;//图片数组

	@ApiModelProperty(name = "userIds", value = "邀请用户id数组", dataType = "int",hidden = true)
	private Integer[] userIds;//邀请用户id数组

	@ApiModelProperty(name = "payType", value = "支付类型 1微信 2支付宝 3余额 4小程序", dataType = "int",hidden = true)
	private Integer payType;//支付类型 1微信 2支付宝 3余额 4小程序

	@ApiModelProperty(name = "payPwd", value = "支付密码", dataType = "String",hidden = true)
	private String payPwd;//支付密码


	@ApiModelProperty(name = "ids", value = "id数组", dataType = "int",hidden = true)
	private Integer[] ids;//id数组

}
