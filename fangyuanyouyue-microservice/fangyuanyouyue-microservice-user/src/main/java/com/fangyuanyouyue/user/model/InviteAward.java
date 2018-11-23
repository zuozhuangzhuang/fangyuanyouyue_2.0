package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 邀请奖励表
 */
@Getter
@Setter
@ToString
public class InviteAward {
    private Integer id;

    private Integer userId;//用户id

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private String levelDesc;//等级描述

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}