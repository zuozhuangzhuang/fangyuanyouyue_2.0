package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 认证店铺申请表
 */
@Getter
@Setter
@ToString
public class UserAuthApply {
    private Integer id;

    private Integer userId;//用户id

    private Integer status;//认证状态 1申请中 2同意 3拒绝

    private Date startTime;//开始时间

    private Date endTime;//结束时间

    private String reason;//拒绝原因

    private Date addTime;//申请时间

    private Date updateTime;//更新时间

    private String phone;//手机号码

    private String nickName;//昵称

}