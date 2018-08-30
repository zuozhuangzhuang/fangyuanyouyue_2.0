package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 积分订单表
 */
@Getter
@Setter
@ToString
public class PointOrder {
    private Integer id;

    private Integer userId;//用户id

    private String orderNo;//订单号

    private Integer goodsId;//商品ID

    private String goodsName;//商品标题

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private String mainImgUrl;//商品主图路径

    private BigDecimal amount;//商品原价

    private BigDecimal freight;//运送费

    private BigDecimal payAmount;//实际支付金额

    private Long point;//所需的积分

    private Integer type;//类型 1虚拟 2实物

    private String description;//商品描述

}