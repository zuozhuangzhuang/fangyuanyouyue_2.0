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

    private String nickName;//用户昵称
    
    private String headImgUrl;//用户头像

    private Date addTime;//添加时间


}