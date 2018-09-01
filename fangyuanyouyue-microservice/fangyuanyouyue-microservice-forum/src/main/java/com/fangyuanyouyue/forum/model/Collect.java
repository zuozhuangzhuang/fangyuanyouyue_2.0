package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 收藏表
 */
@Getter
@Setter
@ToString
public class Collect {
    private Integer id;//唯一自增ID

    private Integer userId;//用户ID

    private Integer collectId;//收藏对象ID

    private Integer collectType;//关注/收藏类型 1商品 2抢购 3视频 4帖子 5鉴赏

    private Integer type;//类型 1关注 2收藏

    private Date addTime;//添加时间

    private Date updateTime;//更新时间
}