package com.fangyuanyouyue.goods.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 购物车详情DTO
 */
public class CartDetailDto {
    private Integer cartDetailId;//购物车详情ID

    private Integer cartId;//购物车id

    private Integer goodsId;//商品ID

    private String name;//商品名称

    private String description;//商品详情

    private BigDecimal price;//商品价格

    private BigDecimal postage;//运费

    private String mainUrl;//商品主图

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

    public Integer getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(Integer cartDetailId) {
        this.cartDetailId = cartDetailId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }
}
