package com.fangyuanyouyue.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品订单下单详情Dto
 */
@Getter
@Setter
@ToString
public class AddOrderDetailDto {

    private Integer goodsId;//商品ID

    private Integer couponId;//优惠券ID

}
