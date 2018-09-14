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

    private Integer status;//是否处理 1已处理 2未处理

    //举报人昵称
    private String nickName;

    //举报人头像
    private String headImgUrl;

    //被举报对象名
    private String goodsName;
}