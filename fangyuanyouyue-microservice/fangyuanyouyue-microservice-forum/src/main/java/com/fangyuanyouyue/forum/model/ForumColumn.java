package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 专栏
 */
@Getter
@Setter
@ToString
public class ForumColumn {
    private Integer id;//唯一自增ID

    private Integer userId;//专栏主用户id

    private String name;//专栏名称

    private String coverImgUrl;//封面图片地址

    private Integer fansCount;//粉丝数

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer isChosen;//是否精选1是 2否

    private Integer typeId;//专栏分类id

    private String typeName;//专栏分类名称

}