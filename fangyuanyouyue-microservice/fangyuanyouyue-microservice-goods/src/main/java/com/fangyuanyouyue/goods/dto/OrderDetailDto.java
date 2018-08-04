package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.OrderDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情表DTO
 */
public class OrderDetailDto {
    private Integer userId;//买家id

    private Integer orderId;//订单id

    private Integer goodsId;//商品id

    private String goodsName;//商品标题

    private BigDecimal amount;//商品价格

    private BigDecimal payAmount;//商品原价

    private BigDecimal freight;//运送费

    private String iconImg;//商品主图

    private String description;//商品详情

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消  7已申请退货

    private Integer allowReturn = 0;//是否可以退货  0可退货  1不可退货

    //优惠券

    public OrderDetailDto() {
    }

    public OrderDetailDto(OrderDetail orderDetail,Integer status) {
        this.userId = orderDetail.getUserId();
        this.orderId = orderDetail.getOrderId();
        this.goodsId = orderDetail.getGoodsId();
        this.goodsName = orderDetail.getGoodsName();
        this.iconImg = orderDetail.getMainImgUrl();
        this.amount = orderDetail.getAmount();
        this.payAmount = orderDetail.getPayAmount();
        this.freight = orderDetail.getFreight();
        this.status = status;
        this.description = orderDetail.getDescription();
//        this.allowReturn = allowReturn;
    }

    public static ArrayList<OrderDetailDto> toDtoList(List<OrderDetail> list,Integer status) {
        if (list == null)
            return null;
        ArrayList<OrderDetailDto> dtolist = new ArrayList<>();
        for (OrderDetail model : list) {
            OrderDetailDto dto = new OrderDetailDto(model,status);
            dtolist.add(dto);
        }
        return dtolist;
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
        this.goodsName = goodsName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public String getIconImg() {
        return iconImg;
    }

    public void setIconImg(String iconImg) {
        this.iconImg = iconImg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAllowReturn() {
        return allowReturn;
    }

    public void setAllowReturn(Integer allowReturn) {
        this.allowReturn = allowReturn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
