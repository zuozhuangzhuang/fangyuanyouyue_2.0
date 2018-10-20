package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 代理表
 */
@Getter
@Setter
@ToString
public class ConfinedUser {
    private Integer id;

    private Integer userId;//用户id

    private Integer status;//代理状态 1代理 2非代理

    private Date addTime;//添加时间

    private Date updateTime;//修改时间

    private Integer parentId;//代理上级id

    private String code;//代理唯一代码

    private Integer canWithdraw=1;//是否可以提现 1可以提现 2无法提现
}