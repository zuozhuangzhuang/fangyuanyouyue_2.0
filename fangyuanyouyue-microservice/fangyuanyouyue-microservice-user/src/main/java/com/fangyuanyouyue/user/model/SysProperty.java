package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 信息配置表
 */
@Getter
@Setter
@ToString
public class SysProperty {
    private Integer id;

    private String keyWord;//信息配置表

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private String value;//具体内容
}