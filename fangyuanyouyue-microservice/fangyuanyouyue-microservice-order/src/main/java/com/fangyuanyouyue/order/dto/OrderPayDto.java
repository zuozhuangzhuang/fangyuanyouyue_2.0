package com.fangyuanyouyue.order.dto;

import com.fangyuanyouyue.order.model.OrderInfo;
import com.fangyuanyouyue.order.model.OrderPay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单支付表dto
 */
public class OrderPayDto {
    private Integer orderId;//订单id

    private String receiverName;//收货人姓名

    private String receiverPhone;//收货人手机

    private String address;//详细地址

    private String postCode;//邮政编码

    private String orderNo;//订单号

    private BigDecimal amount;//订单总额

    private BigDecimal payAmount;//实际支付金额

    private BigDecimal freight;//运费金额

    private Integer count;//商品总数

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货

    public OrderPayDto() {
    }

    public OrderPayDto(OrderPay orderPay) {
        this.orderId = orderPay.getOrderId();
        this.receiverName = orderPay.getReceiverName();
        this.receiverPhone = orderPay.getReceiverPhone();
        this.address = orderPay.getAddress();
        this.postCode = orderPay.getPostCode();
        this.orderNo = orderPay.getOrderNo();
        this.amount = orderPay.getAmount();
        this.payAmount = orderPay.getPayAmount();
        this.freight = orderPay.getFreight();
        this.count = orderPay.getCount();
        this.status = orderPay.getStatus();
    }

    public static ArrayList<OrderPayDto> toDtoList(List<OrderPay> list) {
        if (list == null)
            return null;
        ArrayList<OrderPayDto> dtolist = new ArrayList<>();
        for (OrderPay model : list) {
            OrderPayDto dto = new OrderPayDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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
}
