package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户扩展信息表
 */
@Getter
@Setter
@ToString
public class UserInfoExt {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private String identity;//身份证号码

    private String name;//真实姓名

    private String payPwd;//支付密码，明文6位，MD5小写

    private Integer status;//实名认证状态 1申请 2通过 3未通过

    private Integer authType;//认证店铺状态 1申请 2认证 3未认证

    private Long credit;//信誉度

    private Date addTime;//添加时间

    private Date updateTime;//更新时间


}