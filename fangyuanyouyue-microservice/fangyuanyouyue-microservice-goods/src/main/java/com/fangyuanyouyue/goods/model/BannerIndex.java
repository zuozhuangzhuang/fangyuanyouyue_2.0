package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 首页轮播图表
 */
@Getter
@Setter
@ToString
public class BannerIndex {
    private Integer id;//唯一自增ID

    private Integer businessId;//业务ID:商品ID/用户ID

    private Integer jumpType;//跳转类型,0:商品 1：个人

    private Integer type;//业务类型,0:商品 1：个人

    private String title;//描述标题

    private String imgUrl;//图片地址

    private Integer sort;//排序，由低到高

    private Integer status;//是否下架，0未下架 1下架

    private Date addTime;//添加时间

    private Date updateTime;//更新时间


}