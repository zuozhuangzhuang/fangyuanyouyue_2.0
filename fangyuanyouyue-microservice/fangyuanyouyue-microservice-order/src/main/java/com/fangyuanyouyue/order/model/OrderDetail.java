package com.fangyuanyouyue.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情表
 */
@Getter
@Setter
@ToString
public class OrderDetail {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private Integer orderId;//订单id

    private Integer goodsId;//商品id

    private String goodsName;//商品标题

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer couponId;//优惠券ID

    private String mainImgUrl;//商品主图

    private BigDecimal amount;//商品原价

    private BigDecimal freight;//运送费

    private BigDecimal payAmount;//实际支付金额

    private String description;//商品描述

    private Integer sellerId;//卖家ID

    private Integer mainOrderId;//主订单ID

    private Integer goodsType;//类型 1普通商品 2抢购商品

}