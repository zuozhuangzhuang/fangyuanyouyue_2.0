package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 积分商品表
 */
@Getter
@Setter
@ToString
public class PointGoods {
    private Integer id;

    private String name;//积分商品名称

    private Integer type;//商品类型 1虚拟 2实物

    private Integer price;//价格

    private Long point;//所需积分

    private Integer status;//商品状态 1上架中 2已下架

    private String coverImgUrl;//封面图片URL

    private Integer couponId;//优惠券ID

    private Date addTime;//添加时间

    private Date updateTime;//修改时间

    private String description;//描述

}