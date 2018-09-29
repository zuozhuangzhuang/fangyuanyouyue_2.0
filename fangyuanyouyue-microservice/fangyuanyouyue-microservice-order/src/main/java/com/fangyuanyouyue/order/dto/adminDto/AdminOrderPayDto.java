package com.fangyuanyouyue.order.dto.adminDto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.order.model.OrderPay;
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
public class AdminOrderPayDto {
    private Integer orderId;//订单id

    private String receiverName;//收货人姓名

    private String receiverPhone;//收货人手机

    private String province;//省份

    private String city;//城市

    private String area;//区域

    private String address;//详细地址
    
    private String receiver;//详细收货信息

    private String postCode;//邮政编码

    private String orderNo;//订单号

    private BigDecimal amount;//订单总额

    private BigDecimal payAmount;//实际支付金额

    private String payNo;

    private BigDecimal freight;//运费金额

    private Integer count;//商品总数

    private Integer status;//状态 1待支付 2待发货 3待收货 4已完成 5已取消

    //order_refund
    private Integer returnStatus;//退货状态 1申请退货 2退货成功 3拒绝退货

    private String sellerReturnStatus;//卖家是否同意退货状态 null正常  1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货

    private String payTime;//支付时间

    private String sendTime;//发货时间

    private String logisticCode;//物流单号

    private String logisticCompany;//物流公司

    private Integer payType; //支付方式/支付类型 1微信 2支付宝 3余额 4小程序
    
    public AdminOrderPayDto() {
    }

    public AdminOrderPayDto(OrderPay orderPay) {
    	this.payType = orderPay.getPayType();
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
        if(orderPay.getPayTime() != null){
            this.payTime = DateUtil.getFormatDate(orderPay.getPayTime(),DateUtil.DATE_FORMT);
        }
        if(orderPay.getSendTime() != null){
            this.sendTime = DateUtil.getFormatDate(orderPay.getSendTime(),DateUtil.DATE_FORMT);
        }
        this.logisticCode = orderPay.getLogisticCode();
        this.logisticCompany = orderPay.getLogisticCompany();
        this.receiver =  this.receiverName+" "+this.receiverPhone+"\n" + this.province+this.city+this.area+this.address;
        this.payNo = orderPay.getPayNo();
    }

    public static ArrayList<AdminOrderPayDto> toDtoList(List<OrderPay> list) {
        if (list == null) {
            return null;
        }
        ArrayList<AdminOrderPayDto> dtolist = new ArrayList<>();
        for (OrderPay model : list) {
            AdminOrderPayDto dto = new AdminOrderPayDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
