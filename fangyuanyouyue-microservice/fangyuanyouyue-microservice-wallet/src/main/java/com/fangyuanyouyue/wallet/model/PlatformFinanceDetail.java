package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台收支明细表
 */
@Getter
@Setter
@ToString
public class PlatformFinanceDetail {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private BigDecimal amount;//金额

    private String orderNo;//平台订单号

    private String payNo;//第三方支付流水号

    private Integer payType;//支付类型1微信 2支付宝

    private Integer type;//收支类型

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}