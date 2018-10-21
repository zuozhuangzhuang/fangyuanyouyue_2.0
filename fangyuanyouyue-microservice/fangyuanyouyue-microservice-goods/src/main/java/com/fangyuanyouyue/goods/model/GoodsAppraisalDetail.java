package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 商品鉴定详情表
 */
@Getter
@Setter
@ToString
public class GoodsAppraisalDetail {
    private Integer id;//唯一自增ID

    private Integer orderId;//鉴定id

    private Integer goodsId;//商品id

    private String opinion;//鉴定观点

    private Integer status;//状态 0申请 1真 2假 3存疑 4待支付(在列表中不显示) 5删除

    private String title;//鉴定标题

    private BigDecimal price;//鉴定赏金

    private String description;//描述

    private Date submitTime;//审核时间

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer type;//鉴定类型 1卖家鉴定 2买家鉴定 3我要鉴定

    private Integer isShow;//是否鉴定展示 1是 2否

    private Integer userId;//用户ID

    //用户昵称
    private String nickName;

    //用户会员等级 null非会员 1铂金会员 2至尊会员
    private Integer vipLevel;
}