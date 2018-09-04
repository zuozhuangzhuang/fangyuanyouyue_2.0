package com.fangyuanyouyue.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 订单评论表
 */
@Getter
@Setter
@ToString
public class OrderComment {
    private Integer id;//唯一自增ID

    private Integer orderId;//订单id

    private Integer goodsQuality;//商品质量 1一颗星 2~3~4~5~

    private Integer serviceAttitude;//服务质量 1一颗星 2~3~4~5~

    /*
    差：小于-100分
    低：-100~1000分之间
    中：1000~1万之间
    高：1万~50万之间
    优：50万以上
     */

    private Integer status;//状态 1好评 2中评 3差评

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}