package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 订单支付表
 */
@Getter
@Setter
@ToString
public class OrderPay {
    private Integer id;//唯一自增ID

    private Integer orderId;//订单id

    private String receiverName;//收货人姓名

    private String receiverPhone;//收货人手机

    private String address;//详细地址

    private String postCode;//邮政编码

    private String orderNo;//订单号

    private BigDecimal amount;//订单总额

    private BigDecimal payAmount;//实际支付金额

    private BigDecimal freight;//运费金额

    private Integer count;//商品总数

    private Integer payType;//支付类型

    private String payNo;//支付流水号

    private String payTime;//支付时间

    private Integer sendType;//配送方式

    private Integer logisticStatus;//物流状态

    private String logisticCode;//物流单号

    private String logisticCompany;//物流公司

    private String remarks;//备注

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消
    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private String province;//省份

    private String city;//城市

    private String area;//区域

}