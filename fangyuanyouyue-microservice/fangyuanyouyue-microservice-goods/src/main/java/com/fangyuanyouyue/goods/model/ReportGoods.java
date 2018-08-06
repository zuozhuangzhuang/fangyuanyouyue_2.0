package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 商品举报表
 */
@Getter
@Setter
@ToString
public class ReportGoods {
    private Integer id;//唯一自增ID

    private String reason;//举报原因

    private Integer goodsId;//商品id

    private Integer userId;//用户id

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}