package com.fangyuanyouyue.goods.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 */
public class OrderInfo {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private Integer mainOrderId;//总订单id

    private String orderNo;//订单号

    private BigDecimal amount;//订单总金额

    private Integer count;//商品总数

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(Integer mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}