package com.fangyuanyouyue.order.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品下单信息DTO
 */
public class AddOrderDto {

    private Integer sellerId;//卖家ID

    private List<AddOrderDetailDto> addOrderDetailDtos;//商品订单下单详情Dto列表

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public List<AddOrderDetailDto> getAddOrderDetailDtos() {
        return addOrderDetailDtos;
    }

    public void setAddOrderDetailDtos(String[] data) {
        List<AddOrderDetailDto> addOrderDetailDtos = new ArrayList<>();
        for(String str:data){
            addOrderDetailDtos.add(JSONObject.toJavaObject(JSONObject.parseObject(str),AddOrderDetailDto.class));
        }
        this.addOrderDetailDtos = addOrderDetailDtos;
    }
}
