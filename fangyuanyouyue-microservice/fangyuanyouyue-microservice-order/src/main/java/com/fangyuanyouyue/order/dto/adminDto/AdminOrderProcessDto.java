package com.fangyuanyouyue.order.dto.adminDto;

import com.fangyuanyouyue.order.model.OrderPay;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单统计信息DTO
 */
@Getter
@Setter
@ToString
public class AdminOrderProcessDto {
    //订单统计数量
    private Integer orderCount;

    //订单状态 1待支付 2待发货 3待收货 4已完成 5已取消
    private Integer status;
}
