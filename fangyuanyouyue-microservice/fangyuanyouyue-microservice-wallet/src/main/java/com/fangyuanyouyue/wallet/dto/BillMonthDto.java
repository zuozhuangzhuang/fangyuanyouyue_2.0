package com.fangyuanyouyue.wallet.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 用户余额月账单
 */
@Getter
@Setter
@ToString
public class BillMonthDto {
    private String date;//日期

    private Integer income;//收入

    private Integer disburse;//支出

    private List<BillMonthDto> balanceDtos;//用户账单列表
}
