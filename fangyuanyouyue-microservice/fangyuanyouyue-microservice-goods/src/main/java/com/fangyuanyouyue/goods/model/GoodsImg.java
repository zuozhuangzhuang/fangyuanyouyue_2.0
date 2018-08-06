package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 商品图片表
 */
@Getter
@Setter
@ToString
public class GoodsImg {
    private Integer id;

    private Integer goodsId;//商品id

    private String imgUrl;//图片地址

    private Integer type;//类型 1主图（展示在第一张的图片） 2次图

    private Integer sort;//排序

    private Date addTime;//添加时间

}