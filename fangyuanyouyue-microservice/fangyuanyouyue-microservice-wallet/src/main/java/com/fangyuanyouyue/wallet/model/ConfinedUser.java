package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 限制使用余额用户表
 */
@Getter
@Setter
@ToString
public class ConfinedUser {
    private Integer id;//

    private Integer userId;//用户ID

    private Integer status;//用于假删除用户  0被限制用户 1被限制后又被解除限制用户（表面正常用户）

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}