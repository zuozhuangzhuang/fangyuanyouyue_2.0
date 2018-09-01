package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 帖子图片表
 */
@Getter
@Setter
@ToString
public class ForumImg {
    private Integer id;

    private Integer forumId;//帖子id

    private String imgUrl;//图片路径

    private Date addTime;//添加时间

}