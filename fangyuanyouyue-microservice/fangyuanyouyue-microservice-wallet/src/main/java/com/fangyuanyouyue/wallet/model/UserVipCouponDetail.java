package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 会员发放优惠券记录表
 */
@Getter
@Setter
@ToString
public class UserVipCouponDetail {
    private Integer id;

    private Integer userId;//用户id

    private Date addTime;//添加时间

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private Date vipStartTime;//会员开通时间

    private Integer count;//发放次数
}
