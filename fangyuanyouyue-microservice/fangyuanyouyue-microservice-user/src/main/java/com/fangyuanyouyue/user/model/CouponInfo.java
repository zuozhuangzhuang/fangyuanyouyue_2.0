package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 代金券表
 */
@Getter
@Setter
@ToString
public class CouponInfo {
    private Integer id;

    private Integer couponType;//优惠券类型 1满减 2折扣

    private String couponCode;//优惠券代码

    private BigDecimal couponAmount;//优惠券金额

    private BigDecimal conditionAmount;//满减达到的条件

    private Integer count;//优惠券的数量

    private Date startTime;//有效开始时间

    private Date endTime;//过期时间

    private Integer status;//状态1有效 2无效

    private Date updateTime;//修改时间

    private Date addTime;//添加时间
}