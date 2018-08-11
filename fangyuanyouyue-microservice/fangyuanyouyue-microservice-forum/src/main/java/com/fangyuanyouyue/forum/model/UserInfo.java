package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserInfo {
    private Integer id;

    private String phone;

    private String email;

    private String address;

    private String loginPwd;

    private String nickName;

    private String headImgUrl;

    private String bgImgUrl;

    private Integer gender;

    private String signature;

    private String contact;

    private Integer level;

    private String levelDesc;

    private Integer regType;

    private Integer regPlatform;

    private Integer status;

    private Integer pwdErrCount;

    private Integer payPwdErrCount;

    private Date lastLoginTime;

    private Integer lastLoginPlatform;

    private Date addTime;

    private Date updateTime;

}