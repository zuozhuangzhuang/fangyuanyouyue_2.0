package com.fangyuanyouyue.goods.dto;

import java.util.List;

/**
 * 购物车内店铺DTO
 */
public class CartShopDto {

    private Integer userId;//店铺id

    private String nickName;//店铺名字

    private String headImgUrl;//店铺头像图片地址

    private List<CartDetailDto> cartDetail;//购物车商品详情



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<CartDetailDto> getCartDetail() {
        return cartDetail;
    }

    public void setCartDetail(List<CartDetailDto> cartDetail) {
        this.cartDetail = cartDetail;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
