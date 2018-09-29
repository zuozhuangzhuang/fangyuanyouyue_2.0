package com.fangyuanyouyue.forum.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 专栏类型表
 */
@Getter
@Setter
@ToString
public class ForumColumnType {
    private Integer id;

    private String name;//分类名称

    private Integer sort;//排序

    private Date addTime;//添加时间

    private Date updateTime;//修改时间
}