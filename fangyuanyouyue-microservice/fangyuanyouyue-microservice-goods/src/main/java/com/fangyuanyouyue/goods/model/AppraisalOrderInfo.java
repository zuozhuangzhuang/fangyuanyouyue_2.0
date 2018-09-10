package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 鉴定订单表
 */
@Getter
@Setter
@ToString
public class AppraisalOrderInfo {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private String orderNo;//订单号

    private BigDecimal amount;//订单总金额

    private Integer count;//商品总数

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer status;//状态 1待支付 2已完成 3已删除
}