package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 全民鉴定订单
 */
@Getter
@Setter
@ToString
public class ArgueOrder {
    private Integer id;

    private Integer userId;//用户ID

    private String orderNo;//订单号

    private BigDecimal amount;//订单总金额

    private Integer status;//状态 1待支付 2已完成 3已删除

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private String title;//标题

    private String toUsers;//邀请人

    private String content;//内容描述

    private String imgUrls;//全民鉴定图片信息
}