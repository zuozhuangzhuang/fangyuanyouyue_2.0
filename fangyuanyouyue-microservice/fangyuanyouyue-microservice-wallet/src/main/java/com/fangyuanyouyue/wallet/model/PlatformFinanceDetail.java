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
    private Integer id;

    private Integer userId;//用户id

    private BigDecimal amount;//金额

    private String payNo;//第三方支付流水号

    private Integer payType;//支付类型 1微信 2支付宝 3余额 4小程序

    private Integer type;//收支类型 1收入 2支出

    private Date addTime;//支付时间

    private Date updateTime;//更新时间

    private String title;//订单标题

    private String orderNo;//订单号

    private Integer orderType;//订单类型 1商品 13抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺 11系统修改余额 12商品置顶

    private Integer sellerId;//卖家id

    private Integer buyerId;//买家id

    private String nickName;//用户昵称

}