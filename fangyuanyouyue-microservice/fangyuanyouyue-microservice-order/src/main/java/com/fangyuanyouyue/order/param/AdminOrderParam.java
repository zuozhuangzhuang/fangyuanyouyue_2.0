package com.fangyuanyouyue.order.param;

import com.fangyuanyouyue.base.BasePageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ApiModel(value = "后台管理订单参数类")
@Getter
@Setter
@ToString
public class AdminOrderParam extends BasePageReq{
    //物流公司编号
    @ApiModelProperty(name = "number", value = "物流公司编号", dataType = "int",hidden = true)
    private String number;

    //物流公司名
    @ApiModelProperty(name = "number", value = "物流公司编号", dataType = "int",hidden = true)
    private String name;

    //价格
    @ApiModelProperty(name = "price", value = "价格", dataType = "BigDecimal",hidden = true)
    private BigDecimal price;

}
