package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.OrderPay;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单支付表dto
 */
@Getter
@Setter
@ToString
public class OrderPayDto {
    private Integer orderId;//订单id

    private String receiverName;//收货人姓名

    private String receiverPhone;//收货人手机

    private String province;//省份

    private String city;//城市

    private String area;//区域

    private String address;//详细地址

    private String postCode;//邮政编码

    private String orderNo;//订单号

    private BigDecimal amount;//订单总额

    private BigDecimal payAmount;//实际支付金额

    private BigDecimal freight;//运费金额

    private Integer count;//商品总数

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消 7已申请退货

    public OrderPayDto() {
    }

    public OrderPayDto(OrderPay orderPay) {
        this.orderId = orderPay.getOrderId();
        this.receiverName = orderPay.getReceiverName();
        this.receiverPhone = orderPay.getReceiverPhone();
        this.province = orderPay.getProvince();
        this.city = orderPay.getCity();
        this.area = orderPay.getArea();
        this.address = orderPay.getAddress();
        this.postCode = orderPay.getPostCode();
        this.orderNo = orderPay.getOrderNo();
        this.amount = orderPay.getAmount();
        this.payAmount = orderPay.getPayAmount();
        this.freight = orderPay.getFreight();
        this.count = orderPay.getCount();
        this.status = orderPay.getStatus();
    }

    public static ArrayList<OrderPayDto> toDtoList(List<OrderPay> list) {
        if (list == null)
            return null;
        ArrayList<OrderPayDto> dtolist = new ArrayList<>();
        for (OrderPay model : list) {
            OrderPayDto dto = new OrderPayDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
