package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 官方认证订单表
 */
@Getter
@Setter
@ToString
public class UserAuthOrder {
    private Integer id;

    private Integer userId;//用户ID

    private String orderNo;//订单号

    private BigDecimal amount;//订单总金额

    private Integer status;//状态 1待支付 2已完成 3已删除

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private String title;//订单标题
    
    private	String phone;
    
    private	String nickName;
}