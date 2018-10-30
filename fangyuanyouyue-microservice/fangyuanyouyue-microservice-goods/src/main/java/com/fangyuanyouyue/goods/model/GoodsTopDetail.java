package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 商品置顶详情表
 */
@Getter
@Setter
@ToString
public class GoodsTopDetail {
    private Integer id;

    private Integer userId;//用户id

    private Integer goodsId;//商品id

    private Integer topOrderId;//订单ID

    private Date addTime;//添加时间

    private Date updateTime;//修改时间

    private Integer status;//状态 1置顶 2失效

    private Date endTime;//置顶结束时间
}