package com.fangyuanyouyue.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 订单表
 */
@Getter
@Setter
@ToString
public class OrderInfo {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private Integer mainOrderId;//总订单id

    private String orderNo;//订单号

    private BigDecimal amount;//订单总金额

    private Integer count;//商品总数

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消 6已删除

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer sellerId;//卖家ID

    private Integer isResolve;//是否拆单 1是 2否

    private Integer isRefund = 2;//是否退货 1是 2否

    private Date receiveTime;//收货时间

    //卖家处理时间 平台处理时间
}