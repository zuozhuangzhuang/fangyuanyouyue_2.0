package com.fangyuanyouyue.user.dao;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

@Mapper
public interface ProcessMapper {
    /**
     * 获取昨日注册用户数
     * @return
     */
    Integer getYestoryUser();

    /**
     * 获取昨日发布商品数
     * @return
     */
    Integer getYestoryGoods();

    /**
     * 获取昨日发布抢购数
     * @return
     */
    Integer getYestoryAuction();

    /**
     * 获取昨日官方鉴定数
     * @return
     */
    Integer getYestoryPlatfomAppraisal();

    /**
     * 获取昨日下单数
     * @return
     */
    Integer getYestoryOrder();

    /**
     * 获取昨日发布帖子
     * @return
     */
    Integer getYestoryForum();

    /**
     * 获取昨日发布视频数
     * @return
     */
    Integer getYestoryVideo();

    /**
     * 获取昨日全民鉴定数
     * @return
     */
    Integer getYestoryAppraisal();

    /**
     * 获取昨日开通会员数
     * @return
     */
    Integer getYestoryVip();

    /**
     * 获取平台昨日收入
     * @return
     */
    BigDecimal getYestoryIncome();

    /**
     * 获取平台昨日支出
     * @return
     */
    BigDecimal getYestoryExpend();
}
