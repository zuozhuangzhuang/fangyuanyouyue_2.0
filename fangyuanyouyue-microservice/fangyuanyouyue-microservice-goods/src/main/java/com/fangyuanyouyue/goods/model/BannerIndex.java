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

    private Integer businessId;//业务ID：例：商品id、视频id、积分商城商品id...

    private Integer jumpType;//跳转类型 1页面 2链接 3图片（businessId为空）

    private Integer businessType;//业务类型 1商品详情、2抢购详情、3帖子详情、4全民鉴定详情、5视频详情、6专栏 7积分商品

    private Integer type;//类型 1首页 2商城 3积分商城

    private String title;//描述标题

    private String imgUrl;//图片地址

    private Integer sort;//排序，由低到高

    private Integer status;//是否展示，1展示 2不展示

    private Date addTime;//添加时间

    private Date updateTime;//更新时间


}