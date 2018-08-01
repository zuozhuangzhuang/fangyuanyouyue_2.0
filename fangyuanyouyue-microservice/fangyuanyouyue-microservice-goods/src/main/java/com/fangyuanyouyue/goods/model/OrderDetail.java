package com.fangyuanyouyue.goods.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情DTO
 */
public class OrderDetail {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private Integer orderId;//订单id

    private Integer goodsId;//商品id

    private String goodsName;//商品标题

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer couponId;//优惠券ID

    private String mainImgUrl;//商品主图

    private BigDecimal orgprice;//商品原价

    private BigDecimal freight;//运送费

    private BigDecimal price;//实际支付金额

    private String description;//商品描述

    private Integer sellerId;//卖家ID

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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
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

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl == null ? null : mainImgUrl.trim();
    }

    public BigDecimal getOrgprice() {
        return orgprice;
    }

    public void setOrgprice(BigDecimal orgprice) {
        this.orgprice = orgprice;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}