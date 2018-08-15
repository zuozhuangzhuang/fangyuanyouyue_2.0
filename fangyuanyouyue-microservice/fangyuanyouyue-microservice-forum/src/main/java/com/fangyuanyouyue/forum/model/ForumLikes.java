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
    private Integer id;//

    private Integer userId;//

    private Integer forumId;//

    private Date addTime;//
    
    private String nickName;
    
    private String headImgUrl;


}