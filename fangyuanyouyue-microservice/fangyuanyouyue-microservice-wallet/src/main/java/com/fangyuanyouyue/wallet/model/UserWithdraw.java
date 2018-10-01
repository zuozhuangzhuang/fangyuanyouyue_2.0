package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户钱包表
 */
@Getter
@Setter
@ToString
public class UserWithdraw {
    private Integer id;

    private Integer userId;//用户id

    private BigDecimal amount;//提现金额

    private Integer payType;//提现方式 1微信 2支付宝

    private Integer status;//申请状态 1申请中 2提现成功 3申请拒绝

    private String content;//审核详情

    private String account;//支付宝账号

    private String realName;//真实姓名

    private String nickName;//用户昵称

    private Date addTime;//添加时间

    private BigDecimal serviceCharge;//手续费
}