package com.fangyuanyouyue.message.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户信息表
 */
@Getter
@Setter
@ToString
public class UserInfo {
    private Integer id;

    private String phone;//手机号码

    private String email;//电子邮件

    private String address;//用户所在地

    private String loginPwd;//登录密码,MD5小写

    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    private String bgImgUrl;//背景图片地址

    private Integer gender = 0;//性别，1男 2女 0不确定

    private String signature;//个性签名

    private String contact;//联系电话

    private Integer level;//用户等级

    private String levelDesc;//等级描述

    private Integer regType;//注册来源 1app 2微信小程序

    private Integer regPlatform;//注册平台 1安卓 2IOS 3小程序

    private Integer status;//状态 1正常 2冻结 3删除

    private Integer pwdErrCount;//登录密码错误次数

    private Integer payPwdErrCount;//支付密码错误次数

    private Date lastLoginTime;//最后登录时间

    private Integer lastLoginPlatform;//最后登录平台 1安卓 2IOS 3小程序

    private Date addTime;//注册时间

    private Date updateTime;//更新时间
    
    private Integer isRegHx;//是否已注册环信 1是 2否

}