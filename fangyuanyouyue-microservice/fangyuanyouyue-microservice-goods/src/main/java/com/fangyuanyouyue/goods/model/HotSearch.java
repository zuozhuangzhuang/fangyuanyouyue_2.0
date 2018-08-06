package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 热门搜索表
 */
@Getter
@Setter
@ToString
public class HotSearch {
    private Integer id;//唯一自增ID

    private String name;//搜索名称

    private Integer count;//搜索次数

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}