package com.fangyuanyouyue.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户行为表
 */
@Getter
@Setter
@ToString
public class UserBehavior {
    private Integer id;

    private Integer businessId;//对象id

    private Integer businessType;//对象类型 1用户 2商品、抢购 3商品、抢购评论 4帖子、视频 5帖子、视频评论 6全民鉴定 7全民鉴定评论

    private Integer userId;//用户id

    private Integer type;//行为类型 1点赞 2关注用户 3评论 4购买抢购

    private Integer toUserId;//行为对象所属用户id

    private Date addTime;//添加时间

    private Date updateTime;//修改时间
}