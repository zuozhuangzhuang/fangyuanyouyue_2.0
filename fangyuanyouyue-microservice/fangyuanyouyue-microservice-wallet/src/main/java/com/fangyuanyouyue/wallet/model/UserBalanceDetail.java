package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户收支明细表
 */
@Getter
@Setter
@ToString
public class UserBalanceDetail {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private BigDecimal amount;//金额

    private BigDecimal beforAmount;//操作前金额

    private BigDecimal afterAmount;//操作后金额

    private Integer payType;//支付类型 1微信 2支付宝 3余额 4小程序

    private Integer type;//收支类型 1收入 2支出

    private Date addTime;//支付时间

    private Date updateTime;//更新时间

    private String title;//订单标题

    private String orderNo;//订单号

    private Integer orderType;//订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5申请专栏 6充值 7开通、续费会员 8认证店铺

    private Integer sellerId;//卖家id

    private Integer buyerId;//买家id
}