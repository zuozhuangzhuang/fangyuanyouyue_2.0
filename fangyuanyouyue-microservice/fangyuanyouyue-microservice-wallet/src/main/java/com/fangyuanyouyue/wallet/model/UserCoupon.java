package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户优惠券表
 */
@Getter
@Setter
@ToString
public class UserCoupon {
    private Integer id;

    private Integer userId;

    private Integer couponId;//优惠券ID

    private Integer status;//状态 1未使用 2已使用 3已过期

    private Date addTime;//添加时间

    private Date updateTime;//修改时间

    private BigDecimal couponAmount;//优惠券金额

    private Integer couponType;//优惠券类型 1满减 2折扣

    private BigDecimal conditionAmount;//满减达到的条件

    private Date startTime;//有效开始时间

    private Date endTime;//过期时间
}