package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 菜单表
 */
@Getter
@Setter
@ToString
public class SysMenu {
    private Integer id;

    private String name;//角色名称

    private Integer parentId;//父id

    private String url;//链接url

    private String icon;//图标

    private Integer status;//状态

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}