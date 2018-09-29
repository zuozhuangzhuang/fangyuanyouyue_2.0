package com.fangyuanyouyue.forum.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 鉴定评论点赞
 */
@Getter
@Setter
@ToString
public class AppraisalCommentLikes {
    private Integer id;

    private Integer userId;//点赞用户id

    private Integer commentId;//评论id

    private Date addTime;//添加时间
}