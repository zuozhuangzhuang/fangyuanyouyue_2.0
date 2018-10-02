package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 用户角色表
 */
@Getter
@Setter
@ToString
public class SysUserRole {
    private Integer id;

    private Integer operatorId;//操作员id

    private Integer roleId;//角色id

    private Date addTime;//添加时间

    private Date update;//修改时间
}