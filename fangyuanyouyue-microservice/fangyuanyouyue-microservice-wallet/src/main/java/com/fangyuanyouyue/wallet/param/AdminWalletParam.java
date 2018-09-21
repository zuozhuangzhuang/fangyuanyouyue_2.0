package com.fangyuanyouyue.wallet.param;

import com.fangyuanyouyue.base.BasePageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ApiModel(value = "系统用户钱包相关参数")
@Getter
@Setter
@ToString
public class AdminWalletParam extends BasePageReq{

    @ApiModelProperty(name = "type", value = "支付宝账号(如果提现方式是支付宝，必填)", dataType = "int",hidden = true)
    private Integer type;//收支类型 1收入 2支出 3退款

    @ApiModelProperty(name = "type", value = "订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺", dataType = "int",hidden = true)
    private Integer orderType;//订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺

    @ApiModelProperty(name = "type", value = "支付类型 1微信 2支付宝 3余额 4小程序", dataType = "int",hidden = true)
    private Integer payType;//支付类型 1微信 2支付宝 3余额 4小程序

    @ApiModelProperty(name = "content", value = "内容", dataType = "String",hidden = true)
    private String content;//内容

    @ApiModelProperty(name = "amount", value = "金额", dataType = "BigDecimal",hidden = true)
    private BigDecimal amount;//金额

    @ApiModelProperty(name = "vipLevel", value = "会员等级", dataType = "int",hidden = true)
    private Integer vipLevel;//会员等级

    @ApiModelProperty(name = "vipType", value = "会员类型", dataType = "int",hidden = true)
    private Integer vipType;//会员类型

}
