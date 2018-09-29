package com.fangyuanyouyue.order.dto.adminDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 订单月统计信息DTO
 */
@Getter
@Setter
@ToString
public class AdminMonthProcessDto {
    //日期
    private String date;
    //订单统计列表
    private List<AdminOrderProcessDto> orderProcessDtos;
}
