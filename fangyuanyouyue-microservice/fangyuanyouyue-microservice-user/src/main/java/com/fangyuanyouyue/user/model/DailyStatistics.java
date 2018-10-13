package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 实名认证申请表
 */
@Getter
@Setter
@ToString
public class DailyStatistics {
    private Integer id;

    private Date date;//日期

    private Integer userCount=0;//新注册用户数

    private Integer orderCount=0;//新增下单数

    private BigDecimal incomeCount=new BigDecimal(0);//平台收入

    private BigDecimal expendCount=new BigDecimal(0);//平台支出

    private Integer goodsCount=0;//新增商品数

    private Integer forumCount=0;//新增帖子数

    private Integer auctionCount=0;//新增抢购数

    private Integer videoCount=0;//新增视频数

    private Integer appraisalCount=0;//新增全民鉴定

    private Integer platformAppraisalCount=0;//新增官方鉴定数

    private Integer vipCount=0;//新开通会员数

    private Date addTime;//添加时间

    private Date updateTime;//修改时间
}