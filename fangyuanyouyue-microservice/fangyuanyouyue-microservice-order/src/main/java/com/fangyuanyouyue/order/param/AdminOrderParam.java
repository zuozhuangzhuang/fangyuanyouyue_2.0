package com.fangyuanyouyue.order.param;

import com.fangyuanyouyue.base.BasePageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "后台管理订单参数类")
@Getter
@Setter
@ToString
public class AdminOrderParam extends BasePageReq{
    @ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
    private Integer type;//类型 goodsInfo：1普通商品 2秒杀商品 goodsCategory：1主图 2次图 goodsCategory：1普通 2热门

    @ApiModelProperty(name = "number", value = "物流公司编号", dataType = "int",hidden = true)
    private String number;//物流公司编号

    @ApiModelProperty(name = "orderIds", value = "订单id数组", dataType = "int",hidden = true)
    private Integer[] orderIds;//订单id数组
}
