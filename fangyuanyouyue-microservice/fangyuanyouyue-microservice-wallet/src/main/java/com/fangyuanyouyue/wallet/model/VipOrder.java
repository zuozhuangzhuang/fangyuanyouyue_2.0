package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 用户会员订单表
 */
@Getter
@Setter
@ToString
public class VipOrder {
    private Integer id;

    private Integer userId;//用户ID

    private String orderNo;//订单号

    private BigDecimal amount;//订单总金额

    private Integer status;//状态 1待支付 2已完成 3已删除

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private String levelDesc;//会员等级描述

    private Integer vipType;//会员类型 1一个月 2三个月 3一年会员

    private Integer type;//订单类型 1开通 2续费


}