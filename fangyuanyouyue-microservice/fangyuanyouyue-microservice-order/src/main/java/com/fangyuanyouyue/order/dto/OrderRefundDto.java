package com.fangyuanyouyue.order.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.order.model.OrderRefund;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 退货详情DTO
 */
@Getter
@Setter
@ToString
public class OrderRefundDto {
    private Integer id;//唯一自增ID

    private Integer userId;//退货申请用户id

    private Integer orderId;//订单id

    private String reason;//申请理由

    private String serviceNo;//服务单号

    private String pic1;//图片1

    private String pic2;

    private String pic3;

    private String pic4;

    private String pic5;

    private String pic6;

    private Integer status;//状态 1申请退货 2退货成功 3拒绝退货

    private Integer sellerReturnStatus;//卖家是否同意退货状态 1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货

    private String refuseReason;//拒绝退货理由

    private String addTime;//申请退货时间

    private String updateTime;//修改时间

    private String endTime;//最终（后台管理）处理时间

    private String dealTime;//卖家处理时间

    public OrderRefundDto() {
    }

    public OrderRefundDto(OrderRefund refund) {
        this.id = refund.getId();
        this.userId = refund.getUserId();
        this.orderId = refund.getOrderId();
        this.reason = refund.getReason();
        this.serviceNo = refund.getServiceNo();
        this.pic1 = refund.getPic1();
        this.pic2 = refund.getPic2();
        this.pic3 = refund.getPic3();
        this.pic4 = refund.getPic4();
        this.pic5 = refund.getPic5();
        this.pic6 = refund.getPic6();
        this.status = refund.getStatus();
        this.sellerReturnStatus = refund.getSellerReturnStatus();
        this.refuseReason = refund.getRefuseReason();
        this.addTime =  DateUtil.getFormatDate(refund.getAddTime(), DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(refund.getUpdateTime(), DateUtil.DATE_FORMT);
        if(refund.getEndTime() != null){
            this.endTime = DateUtil.getFormatDate(refund.getEndTime(), DateUtil.DATE_FORMT);
        }
        if(refund.getDealTime() != null){
            this.dealTime = DateUtil.getFormatDate(refund.getDealTime(), DateUtil.DATE_FORMT);
        }
    }
}
