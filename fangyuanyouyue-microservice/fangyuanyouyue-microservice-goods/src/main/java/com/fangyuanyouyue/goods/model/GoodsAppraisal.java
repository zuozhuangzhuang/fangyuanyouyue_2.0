package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 商品鉴定表
 */
@Getter
@Setter
@ToString
public class GoodsAppraisal {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private String imgurl;//图片地址

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}