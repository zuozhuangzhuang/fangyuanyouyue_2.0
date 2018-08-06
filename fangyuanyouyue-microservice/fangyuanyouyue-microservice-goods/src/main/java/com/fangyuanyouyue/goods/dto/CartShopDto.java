package com.fangyuanyouyue.goods.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 购物车内店铺DTO
 */
@Getter
@Setter
@ToString
public class CartShopDto {

    private Integer userId;//店铺id

    private String nickName;//店铺名字

    private String headImgUrl;//店铺头像图片地址

    private List<CartDetailDto> cartDetail;//购物车商品详情


}
