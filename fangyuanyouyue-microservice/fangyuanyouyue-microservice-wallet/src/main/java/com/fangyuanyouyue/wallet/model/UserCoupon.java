package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    private Integer couponId;

    private Integer status;

    private Date addTime;

    private Date updateTime;
}