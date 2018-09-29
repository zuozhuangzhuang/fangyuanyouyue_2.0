package com.fangyuanyouyue.user.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 统计信息dto
 */
@Getter
@Setter
@ToString
public class AdminProcessDto {
    //今日订单总数
    private Integer todayOrder=0;
    //订单总数
    private Integer allOrder=0;
    //今日注册用户总数
    private Integer todayUser=0;
    //注册用户总数
    private Integer allUser=0;
    //今日商品总数
    private Integer todayGoods=0;
    //商品总数
    private Integer allGoods=0;
    //今日帖子总数
    private Integer todayForum=0;
    //帖子总数
    private Integer allForum=0;
}
