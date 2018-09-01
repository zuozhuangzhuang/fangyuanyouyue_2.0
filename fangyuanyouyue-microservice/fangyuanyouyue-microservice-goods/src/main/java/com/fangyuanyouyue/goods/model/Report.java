package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 举报表
 */
@Getter
@Setter
@ToString
public class Report {
    private Integer id;//唯一自增ID

    private String reason;//举报原因

    private Integer businessId;

    private Integer userId;//用户id

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer type;//举报类型 1商品\抢购 2视频 3帖子 4全民鉴定 5用户
}