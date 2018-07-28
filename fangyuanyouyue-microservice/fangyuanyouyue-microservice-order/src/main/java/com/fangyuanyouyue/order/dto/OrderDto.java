package com.fangyuanyouyue.order.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.order.model.OrderInfo;

/**
 * 订单DTO
 */
public class OrderDto {
    //订单信息
    private Integer userId;//买家id

    private String nickName;//买家昵称

    private Integer orderId;//订单ID

    private String orderNo;//订单号

    private BigDecimal totalAmount;//订单总额

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货

    //商品信息
    private List<OrderDetailDto> orderDetailDtos;//订单商品列表
    //订单支付信息 收货地址信息支付信息中
    private OrderPayDto orderPayDto;//订单支付表

    private String addTime;//下单时间

    public OrderDto() {
    }

    public OrderDto(OrderInfo orderInfo) {
        this.userId = orderInfo.getUserId();
        this.orderId = orderInfo.getId();
        this.orderNo = orderInfo.getOrderNo();
        this.totalAmount = orderInfo.getAmount();
        this.status = orderInfo.getStatus();
        this.addTime = DateUtil.getFormatDate(orderInfo.getAddTime(), DateUtil.DATE_FORMT);
    }

    public static ArrayList<OrderDto> toDtoList(List<OrderInfo> list) {
        if (list == null)
            return null;
        ArrayList<OrderDto> dtolist = new ArrayList<>();
        for (OrderInfo model : list) {
            OrderDto dto = new OrderDto(model);
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OrderDetailDto> getOrderDetailDtos() {
        return orderDetailDtos;
    }

    public void setOrderDetailDtos(List<OrderDetailDto> orderDetailDtos) {
        this.orderDetailDtos = orderDetailDtos;
    }

    public OrderPayDto getOrderPayDto() {
        return orderPayDto;
    }

    public void setOrderPayDto(OrderPayDto orderPayDto) {
        this.orderPayDto = orderPayDto;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
