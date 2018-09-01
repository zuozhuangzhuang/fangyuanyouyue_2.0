package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户充值明细表
 */
@Getter
@Setter
@ToString
public class UserRechargeDetail {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private BigDecimal amount;//金额

    private Integer payType;//支付类型 1微信 2支付宝

    private String payNo;//第三方支付流水号

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer status;//状态 1待支付 2已完成 3已删除
}