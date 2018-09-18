package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 用户积分收支账单
 */
@Getter
@Setter
@ToString
public class UserScoreDetail {
    private Integer id;

    private Integer userId;//用户id

    private Long score;//分值

    private Date addTime;//添加时间

    private Date updateTime;//修改时间

    private Integer type;//收支类型 1收入 2支出

    private String orderNo;//订单号

}