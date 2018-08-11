package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.goods.model.AppraisalOrderInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 鉴定订单表DTO
 */
@Getter
@Setter
@ToString
public class AppraisalOrderInfoDto {
    private Integer orderId;//订单ID

    private Integer userId;//用户id

    private Integer appraisalId;//鉴定id

    private String orderNo;//订单号

    private BigDecimal amount;//订单总金额

    private Integer count;//商品总数

    private String addTime;//添加时间

    private String updateTime;//更新时间

    public AppraisalOrderInfoDto() {
    }

    public AppraisalOrderInfoDto(AppraisalOrderInfo appraisalOrderInfo) {
        this.orderId = appraisalOrderInfo.getId();
        this.userId = appraisalOrderInfo.getUserId();
        this.orderNo = appraisalOrderInfo.getOrderNo();
        this.amount = appraisalOrderInfo.getAmount();
        this.count = appraisalOrderInfo.getCount();
        this.addTime = DateUtil.getFormatDate(appraisalOrderInfo.getAddTime(), DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(appraisalOrderInfo.getAddTime(), DateUtil.DATE_FORMT);
    }
}
