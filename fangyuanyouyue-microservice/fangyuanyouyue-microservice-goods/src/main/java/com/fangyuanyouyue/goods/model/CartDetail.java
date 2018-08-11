package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 购物车明细表
 */
@Getter
@Setter
@ToString
public class CartDetail {
    private Integer id;//唯一自增ID

    private Integer cartId;//购物车id

    private Integer goodsId;//商品ID

    private BigDecimal price;//商品价格

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer userId;//店铺ID

}