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

    private Integer userId;//评论用户id

    private Integer forumId;//帖子id

    private Integer commentId;//评论id

    private String content;//评论内容

    private Integer status;//状态1显示 2隐藏

    private Date addTime;//添加时间

    private Date updateTime;//修改时间
    
    private String headImgUrl;//用户头像
    
    private String nickName;//用户昵称

}