package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 商品评论表
 */
@Getter
@Setter
@ToString
public class GoodsComment {
    private Integer id;

    private Integer goodsId;//商品id

    private Integer userId;//用户id

    private Integer commentId;//回复评论id

    private String content;//评论内容

    private Integer likesCount;//点赞次数

    private String img1Url;//图片地址1

    private String img2Url;//图片地址2

    private String img3Url;//图片地址3

    private Integer status;//状态 1正常 2隐藏

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}