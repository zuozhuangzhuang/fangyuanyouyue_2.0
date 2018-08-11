package com.fangyuanyouyue.order.dto;

import com.fangyuanyouyue.order.model.OrderDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情表DTO
 */
@Getter
@Setter
@ToString
public class OrderDetailDto {
    private Integer userId;//买家id

    private Integer sellerId;//卖家id

    private Integer orderId;//订单id

    private Integer goodsId;//商品id

    private String goodsName;//商品标题

    private BigDecimal amount;//商品价格

    private BigDecimal payAmount;//商品原价

    private BigDecimal freight;//运送费

    private String iconImg;//商品主图

    private String description;//商品详情

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消 6已删除 7已申请退货

    private Integer allowReturn = 0;//是否可以退货  0可退货  1不可退货

    //order_refund
    private Integer returnStatus;//退货状态 1申请退货 2退货成功 3拒绝退货

    private String sellerReturnStatus;//卖家是否同意退货状态 null正常  1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货


    //优惠券

    public OrderDetailDto() {
    }

    public OrderDetailDto(OrderDetail orderDetail,Integer status) {
        this.userId = orderDetail.getUserId();
        this.sellerId = orderDetail.getSellerId();
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

    public static ArrayList<OrderDetailDto> toDtoList(List<OrderDetail> list, Integer status) {
        if (list == null)
            return null;
        ArrayList<OrderDetailDto> dtolist = new ArrayList<>();
        for (OrderDetail model : list) {
            OrderDetailDto dto = new OrderDetailDto(model,status);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
