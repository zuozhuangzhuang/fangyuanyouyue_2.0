package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 角色菜单表
 */
@Getter
@Setter
@ToString
public class SysRoleMenu {
    private Integer id;

    private Integer roleId;//角色id

    private Integer menuId;//菜单id

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}