package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户会员信息表
 */
@Getter
@Setter
@ToString
public class UserVip {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id，user_info id

    private Date startTime;//会员开通时间

    private Date endTime;//会员过期时间

    private Integer vipLevel;//会员等级 1铂金会员 2至尊会员

    private String levelDesc;//会员等级描述

    private Integer vipType;//会员类型 1体验会员 2月会员 3年会员

    private Integer status;//会员状态1已开通 2未开通

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}