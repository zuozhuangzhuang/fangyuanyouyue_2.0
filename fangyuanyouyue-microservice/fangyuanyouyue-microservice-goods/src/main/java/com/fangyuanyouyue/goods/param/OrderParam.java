package com.fangyuanyouyue.goods.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;

@ApiModel(value = "订单参数类")
public class OrderParam {
    //公用

    @ApiModelProperty(name = "token", value = "用户token", dataType = "String",hidden = true)
    private String token;//用户token

    @ApiModelProperty(name = "orderId", value = "订单ID", dataType = "int",hidden = true)
    private Integer orderId;//订单ID

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
                "token='" + token + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
