package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.model.OrderInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单DTO
 */
@Getter
@Setter
@ToString
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
}
