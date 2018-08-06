package com.fangyuanyouyue.goods.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;

@ApiModel(value = "订单参数类")
@Getter
@Setter
@ToString
public class OrderParam {
    //公用

    @ApiModelProperty(name = "token", value = "用户token", dataType = "String",hidden = true)
    private String token;//用户token

    @ApiModelProperty(name = "orderId", value = "订单ID", dataType = "int",hidden = true)
    private Integer orderId;//订单ID

}
