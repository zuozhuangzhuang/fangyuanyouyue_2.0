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
public class UserWallet {
    private Integer id;//

    private Integer userId;//用户id

    private BigDecimal balance;//账户余额

    private BigDecimal balanceFrozen;//冻结余额

    private Long point;//积分余额

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}