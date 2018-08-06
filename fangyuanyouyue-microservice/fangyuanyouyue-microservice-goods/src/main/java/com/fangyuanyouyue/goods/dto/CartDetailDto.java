package com.fangyuanyouyue.goods.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 购物车详情DTO
 */
@Getter
@Setter
@ToString
public class CartDetailDto {
    private Integer cartDetailId;//购物车详情ID

    private Integer cartId;//购物车id

    private Integer goodsId;//商品ID

    private String name;//商品名称

    private String description;//商品详情

    private BigDecimal price;//商品价格

    private BigDecimal postage;//运费

    private String mainUrl;//商品主图

    private Integer status;//商品状态 1出售中 2已售出

    public CartDetailDto() {
    }

    public CartDetailDto(Map<String,Object> map) {
        this.cartDetailId = (Integer)map.get("id");
        this.cartId = (Integer)map.get("cart_id");
        this.goodsId = (Integer)map.get("goods_id");
        this.name = (String)map.get("name");
        this.description = (String)map.get("description");
        this.price = (BigDecimal)map.get("price");
        this.postage = (BigDecimal)map.get("postage");
        this.status = (Integer)map.get("status2");
    }

    public static List<CartDetailDto> toDtoList(List<Map<String,Object>> list) {
        if (list == null)
            return null;
        List<CartDetailDto> dtolist = new ArrayList<>();
        for (Map<String,Object> model : list) {
            CartDetailDto dto = new CartDetailDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
