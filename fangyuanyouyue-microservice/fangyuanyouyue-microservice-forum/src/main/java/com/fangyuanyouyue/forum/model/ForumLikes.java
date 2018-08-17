package com.fangyuanyouyue.forum.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    private String nickName;
    
    private String headImgUrl;

    private Date addTime;//添加时间


}