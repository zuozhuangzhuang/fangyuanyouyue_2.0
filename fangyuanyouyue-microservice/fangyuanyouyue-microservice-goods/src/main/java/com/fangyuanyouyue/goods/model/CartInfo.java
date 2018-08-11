package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 购物车表
 */
@Getter
@Setter
@ToString
public class CartInfo {
    private Integer id;

    private Integer userId;//用户id

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}