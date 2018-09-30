package com.fangyuanyouyue.forum.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 专栏订单表
 */
@Getter
@Setter
@ToString
public class ColumnOrder {
    private Integer id;

    private Integer userId;//用户ID

    private String orderNo;//订单号

    private BigDecimal amount;//订单总金额

    private Integer status;//状态 1待支付 2已完成 3已删除

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer typeId;//专栏分类id

    private String name;//专栏名称

    private String payNo;//支付流水号
}