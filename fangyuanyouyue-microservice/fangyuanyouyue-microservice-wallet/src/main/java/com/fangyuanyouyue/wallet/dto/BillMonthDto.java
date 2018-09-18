package com.fangyuanyouyue.wallet.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户月收支dto
 */
@Getter
@Setter
@ToString
public class BillMonthDto {
    private BigDecimal income;//月收入

    private BigDecimal disburse;//月支出

//    private List<UserBalanceDto> balanceDtos;//用户账单列表
}
