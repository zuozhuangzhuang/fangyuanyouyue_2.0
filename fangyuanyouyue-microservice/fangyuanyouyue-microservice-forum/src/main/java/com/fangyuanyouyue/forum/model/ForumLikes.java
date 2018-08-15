package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 论坛帖子点赞表
 */
@Getter
@Setter
@ToString
public class ForumLikes {
    private Integer id;//自增id

    private Integer userId;//用户id

    private Integer forumId;//帖子id

    private Date addTime;//添加时间


}