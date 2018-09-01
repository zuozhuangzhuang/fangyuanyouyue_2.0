package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 抢购降价历史表
 */
@Getter
@Setter
@ToString
public class GoodsIntervalHistory {
    private Integer id;

    private Integer goodsId;//抢购id

    private BigDecimal markdown;//降价幅度

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private BigDecimal previousPrice;//降价前价格

}