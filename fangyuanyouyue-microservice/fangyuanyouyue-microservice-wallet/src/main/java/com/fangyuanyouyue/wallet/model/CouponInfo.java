package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class CouponInfo {
    private Integer id;

    private Integer couponType;

    private String couponCode;

    private BigDecimal couponAmount;

    private BigDecimal conditionAmount;

    private Integer count;

    private Date startTime;

    private Date endTime;

    private Integer status;

    private Date updateTime;

    private Date addTime;

}