package com.fangyuanyouyue.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 店铺信息DTO
 */
@Getter
@Setter
@ToString
public class SellerDto {
    private String sellerName;//卖家昵称

    private Integer sellerId;//卖家ID

    private String sellerHeadImgUrl;//卖家头像


    public SellerDto() {
    }


}
