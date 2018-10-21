package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 商品议价表
 */
@Getter
@Setter
@ToString
public class GoodsBargain {
    private Integer id;//唯一自增ID

    private Integer userId;//申请用户id

    private Integer goodsId;//商品id

    private BigDecimal price;//出价钱

    private String reason;//议价理由

    private Integer status;//状态 1申请 2同意 3拒绝 4取消

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer addressId;//收货地址ID

    private Integer isDelete;//是否删除 1是 2否

    private String bargainNo;//议价订单号

    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

}