package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 商品分类关联表
 */
@Getter
@Setter
@ToString
public class GoodsCorrelation {
    private Integer id;//唯一自增ID

    private Integer goodsCategoryId;//商品分类ID

    private Integer goodsId;//商品ID

    private Integer categoryParentId;//商品分类父ID

    private Date addTime;//添加时间

    private Date updateTime;//更新时间


}