package com.fangyuanyouyue.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 订单日志表
 */
@Getter
@Setter
@ToString
public class OrderLog {
    private Integer id;//唯一自增ID

    private Integer orderId;//订单id

    private String operator;//操作人

    private String content;//内容

    private Date addTime;//添加时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }
}