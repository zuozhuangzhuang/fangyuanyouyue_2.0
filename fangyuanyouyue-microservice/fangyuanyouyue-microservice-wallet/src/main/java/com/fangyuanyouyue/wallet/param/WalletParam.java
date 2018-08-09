package com.fangyuanyouyue.wallet.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ApiModel(value = "用户钱包相关参数")
@Getter
@Setter
@ToString
public class WalletParam {
    @ApiModelProperty(name = "userId", value = "用户ID", dataType = "int",hidden = true)
    private Integer userId;//用户ID

    @ApiModelProperty(name = "price", value = "价格", dataType = "BigDecimal",hidden = true)
    private BigDecimal price;//价格

    @ApiModelProperty(name = "token", value = "用户token", dataType = "String",hidden = true)
    private String token;//用户token

    @ApiModelProperty(name = "type", value = "类型", dataType = "int",hidden = true)
    private Integer type;//类型

    @ApiModelProperty(name = "type", value = "支付宝账号(如果提现方式是支付宝，必填)", dataType = "String",hidden = true)
    private String account;//支付宝账号(如果提现方式是支付宝，必填)

    @ApiModelProperty(name = "type", value = "真实姓名", dataType = "String",hidden = true)
    private String realName;//真实姓名

    @ApiModelProperty(name = "type", value = "支付密码", dataType = "String",hidden = true)
    private String payPwd;//支付密码

    @ApiModelProperty(name = "score", value = "积分值", dataType = "long",hidden = true)
    private Long score;//积分值

    @ApiModelProperty(name = "amount", value = "金额", dataType = "BigDecimal",hidden = true)
    private BigDecimal amount;//金额

    @ApiModelProperty(name = "vipLevel", value = "会员等级", dataType = "int",hidden = true)
    private Integer vipLevel;//会员等级

    @ApiModelProperty(name = "vipType", value = "会员类型", dataType = "int",hidden = true)
    private Integer vipType;//会员类型

}
