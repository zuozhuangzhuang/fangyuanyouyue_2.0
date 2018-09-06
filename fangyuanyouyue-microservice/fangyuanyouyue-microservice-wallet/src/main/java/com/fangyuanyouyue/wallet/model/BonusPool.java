package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 奖池
 */
@Getter
@Setter
@ToString
public class BonusPool {
    private Integer id;//唯一自增ID

    private String bonusName;//奖品名字

    private Double probability;//中奖概率

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer type;//类型 0谢谢惠顾 1积分 2优惠券

    private Long score;//积分值

    private Integer couponId;//优惠券id
}