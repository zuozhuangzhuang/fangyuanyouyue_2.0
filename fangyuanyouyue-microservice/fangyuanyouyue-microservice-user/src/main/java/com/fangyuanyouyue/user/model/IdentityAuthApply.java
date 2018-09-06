package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 实名认证申请表
 */
@Getter
@Setter
@ToString
public class IdentityAuthApply {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private String name;//姓名

    private String identity;//身份证号码

    private String identityImgCover;//身份证封面图

    private String identityImgBack;//身份证背面

    private String rejectDesc;//拒绝原因

    private Integer status;//状态 1申请 2通过 3拒绝

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}