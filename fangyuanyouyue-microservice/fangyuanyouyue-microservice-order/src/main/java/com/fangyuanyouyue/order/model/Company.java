package com.fangyuanyouyue.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流公司表
 */
@Getter
@Setter
@ToString
public class Company {
    private Integer id;//唯一自增ID

    private String companyNo;//公司编号

    private String name;//物流公司名

    private BigDecimal price;//参考价格

    private Integer status;//状态 1正常 2删除

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}