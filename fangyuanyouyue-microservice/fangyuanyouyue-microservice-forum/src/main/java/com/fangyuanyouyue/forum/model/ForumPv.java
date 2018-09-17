package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 论坛帖子浏览表
 */
@Getter
@Setter
@ToString
public class ForumPv {
    private Integer id;//

    private Integer userId;//用户id

    private Integer forumId;//帖子id

    private Date addTime;//添加时间

    private Integer columnId;//专栏id

    private Integer type;//帖子类型 1帖子 2视频
}