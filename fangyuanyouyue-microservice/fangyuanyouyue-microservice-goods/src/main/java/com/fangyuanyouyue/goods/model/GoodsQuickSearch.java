package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品快速搜索条件表
 */
@Getter
@Setter
@ToString
public class GoodsQuickSearch {
    private Integer id;//唯一自增ID

    private BigDecimal priceMin;//最小价格

    private BigDecimal priceMax;//最大价格

    private Integer sort;//显示顺序

    private Integer searchCount;//搜索次数

    private Integer status;//状态 1显示 2不显示

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}