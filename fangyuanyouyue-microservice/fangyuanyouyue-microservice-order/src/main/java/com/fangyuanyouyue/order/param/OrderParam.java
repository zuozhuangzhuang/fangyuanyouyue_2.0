package com.fangyuanyouyue.order.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;

@ApiModel(value = "订单参数类")
public class OrderParam{
    //公用
    @ApiModelProperty(name = "start", value = "起始页", dataType = "int",hidden = true)
    private Integer start; // 起始页
    @ApiModelProperty(name = "limit", value = "限制页", dataType = "int",hidden = true)
    private Integer limit; // 限制页
    @ApiModelProperty(name = "name", value = "搜素字段", dataType = "String",hidden = true)
    private String name; // 搜素字段
    @ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
    private Integer type;//类型 goodsInfo：1普通商品 2秒杀商品 goodsCategory：1主图 2次图 goodsCategory：1普通 2热门


    @ApiModelProperty(name = "token", value = "用户token", dataType = "String",hidden = true)
    private String token;//用户token

    @ApiModelProperty(name = "goodsIds", value = "商品ID数组", dataType = "int",hidden = true)
    private Integer[] goodsIds;//商品ID数组

    @ApiModelProperty(name = "addressId", value = "收货地址id", dataType = "int",hidden = true)
    private Integer addressId;//收货地址id

    @ApiModelProperty(name = "orderId", value = "订单ID", dataType = "int",hidden = true)
    private Integer orderId;//订单ID

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer[] getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(Integer[] goodsIds) {
        this.goodsIds = goodsIds;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderParam{" +
                "start=" + start +
                ", limit=" + limit +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", token='" + token + '\'' +
                ", goodsIds=" + Arrays.toString(goodsIds) +
                ", addressId=" + addressId +
                ", orderId=" + orderId +
                '}';
    }
}
