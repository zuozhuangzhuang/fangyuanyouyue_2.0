package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 操作员表
 */
@Getter
@Setter
@ToString
public class SysOperator {
    private Integer id;

    private String userCode;//登录名

    private String userName;//姓名

    private String loginPwd;//登录密码

    private Integer roleId;//角色

    private String qq;//QQ

    private String email;//邮箱

    private String phone;//手机号码

    private Integer status;//状态

    private String remarks;//备注

    private Date lastLoginTime;//最后登录时间

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}