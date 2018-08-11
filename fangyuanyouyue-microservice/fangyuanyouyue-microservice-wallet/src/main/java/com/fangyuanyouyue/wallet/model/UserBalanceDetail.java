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

    private Integer payType;//支付类型 1微信 2支付宝 3余额

    private Integer type;//收支类型 1收入 2支出

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}