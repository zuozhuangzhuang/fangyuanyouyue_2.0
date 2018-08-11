package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ForumImg {
    private Integer id;

    private Integer forumId;

    private String imgUrl;

    private Date addTime;

}