package com.fangyuanyouyue.forum.model;

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
    //唯一自增ID
    private Integer id;

    //举报原因
    private String reason;

    //被举报对象id
    private Integer businessId;

    //用户id
    private Integer userId;

    //添加时间
    private Date addTime;

    //更新时间
    private Date updateTime;

    //举报类型 1商品\抢购 2视频 3帖子 4全民鉴定
    private Integer type;

    //是否处理 1已处理 2未处理
    private Integer status;

    //举报人昵称
    private String nickName;

    //举报人头像
    private String headImgUrl;

    //被举报对象名
    private String name;

}