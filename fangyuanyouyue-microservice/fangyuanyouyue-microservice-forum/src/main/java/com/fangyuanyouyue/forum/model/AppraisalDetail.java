package com.fangyuanyouyue.forum.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppraisalDetail {
    private Integer id;//

    private Integer userId;//用户id

    private String title;//标题

    private BigDecimal bonus;//赏金

    private String label;//标签

    private Integer sort;//排序 1置顶 2默认排序

    private Integer status;//状态 1进行中 2结束 3删除

    private Date endTime;//结束时间

    private Date updateTime;//修改时间

    private Date addTime;//添加时间

    private String content;//内容描述

    private Integer pvCount;//浏览量基数

    private String headImgUrl;//用户头像

    private String nickName;//用户昵称
}