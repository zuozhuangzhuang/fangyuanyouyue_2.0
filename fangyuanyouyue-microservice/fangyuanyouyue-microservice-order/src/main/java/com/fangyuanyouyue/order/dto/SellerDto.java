package com.fangyuanyouyue.order.dto;

/**
 * 店铺信息DTO
 */
public class SellerDto {
    private String sellerName;//卖家昵称

    private Integer sellerId;//卖家ID

    private String sellerHeadImgUrl;//卖家头像


    public SellerDto() {
    }


    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerHeadImgUrl() {
        return sellerHeadImgUrl;
    }

    public void setSellerHeadImgUrl(String sellerHeadImgUrl) {
        this.sellerHeadImgUrl = sellerHeadImgUrl;
    }

}
