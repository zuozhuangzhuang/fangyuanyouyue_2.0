package com.fangyuanyouyue.order.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "订单参数类")
@Getter
@Setter
@ToString
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


    @ApiModelProperty(name = "sellerList", value = "商品信息数组", dataType = "String",hidden = true)
    private String sellerList;//商品信息数组


    @ApiModelProperty(name = "addressId", value = "收货地址id", dataType = "int",hidden = true)
    private Integer addressId;//收货地址id

    @ApiModelProperty(name = "orderId", value = "订单ID", dataType = "int",hidden = true)
    private Integer orderId;//订单ID

    @ApiModelProperty(name = "status", value = "订单状态 0 待付款  1待收货  2已完成  3全部", dataType = "int",hidden = true)
    private Integer status;//订单状态 0 待付款  1待收货  2已完成  3全部

    @ApiModelProperty(name = "goodsId", value = "商品ID", dataType = "int",hidden = true)
    private Integer goodsId;//商品ID

    @ApiModelProperty(name = "couponId", value = "优惠券ID", dataType = "int",hidden = true)
    private Integer couponId;//优惠券ID

    @ApiModelProperty(name = "payPwd", value = "支付密码", dataType = "String",hidden = true)
    private String payPwd;//支付密码
}
