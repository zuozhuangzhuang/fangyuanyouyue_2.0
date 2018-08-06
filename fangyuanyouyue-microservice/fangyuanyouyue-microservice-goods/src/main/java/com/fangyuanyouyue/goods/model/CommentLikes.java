package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 评论点赞表
 */
@Getter
@Setter
@ToString
public class CommentLikes {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id

    private Integer commentId;//评论id

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

}