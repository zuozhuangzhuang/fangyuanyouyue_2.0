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

    private Integer goodsType;//类型 1普通商品 2抢购商品

    private UserCouponDto userCouponDto;//优惠券信息

    //优惠券

    public OrderDetailDto() {
    }

    public OrderDetailDto(OrderDetail orderDetail) {
        this.userId = orderDetail.getUserId();
        this.sellerId = orderDetail.getSellerId();
        this.orderId = orderDetail.getOrderId();
        this.goodsId = orderDetail.getGoodsId();
        this.goodsName = orderDetail.getGoodsName();
        this.iconImg = orderDetail.getMainImgUrl();
        this.amount = orderDetail.getAmount();
        this.payAmount = orderDetail.getPayAmount();
        this.freight = orderDetail.getFreight();
        this.description = orderDetail.getDescription();
        this.goodsType = orderDetail.getGoodsType();
    }

    public static ArrayList<OrderDetailDto> toDtoList(List<OrderDetail> list) {
        if (list == null) {
            return null;
        }
        ArrayList<OrderDetailDto> dtolist = new ArrayList<>();
        for (OrderDetail model : list) {
            OrderDetailDto dto = new OrderDetailDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
