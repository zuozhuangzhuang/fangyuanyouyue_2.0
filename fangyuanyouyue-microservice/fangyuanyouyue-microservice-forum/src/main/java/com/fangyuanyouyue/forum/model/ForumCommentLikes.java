package com.fangyuanyouyue.forum.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 评论点赞表
 */
@Getter
@Setter
@ToString
public class ForumCommentLikes {
    private Integer id;

    private Integer userId;//点赞用户id

    private Integer commentId;//评论id

    private Date addTime;//添加时间

}