package com.fangyuanyouyue.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
/**
 * 退货表
 */
@Getter
@Setter
@ToString
public class OrderRefund {
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

    private Date addTime;//退货时间

    private Date updateTime;//修改时间

    private Integer sellerReturnStatus;//卖家是否同意退货状态 1申请退货 2卖家直接同意退货 3卖家直接拒绝退货 4卖家48h不处理默认同意退货 5卖家72h小时不处理默认不同意退货

    private String refuseReason;//拒绝退货理由

    private Date endTime;//最终（后台管理）处理时间

    private Date dealTime;//卖家处理时间

}