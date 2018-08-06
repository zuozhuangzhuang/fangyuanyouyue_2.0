package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 帖子评论
 * @author wuzhimin
 *
 */
@Getter
@Setter
@ToString
public class ForumComment {
	
    private Integer id;

    private Integer userId;

    private Integer forumId;

    private Integer commentId;

    private String content;

    private Integer status;

    private Date addTime;

    private Date updateTime;
    
    private String headImgUrl;
    
    private String nickName;
    

}