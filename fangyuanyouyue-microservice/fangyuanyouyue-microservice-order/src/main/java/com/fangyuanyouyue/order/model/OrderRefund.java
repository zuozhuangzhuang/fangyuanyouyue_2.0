package com.fangyuanyouyue.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 退货表
 */
@Getter
@Setter
@ToString
public class OrderRefund {
    private Integer id;//唯一自增ID

    private Integer orderId;//订单id

    private String reason;//申请理由

    private String serviceNo;//服务单号

    private String pic1;//图片1

    private String pic2;

    private String pic3;

    private String pic4;

    private String pic5;

    private String pic6;

    private Integer status;//状态

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}