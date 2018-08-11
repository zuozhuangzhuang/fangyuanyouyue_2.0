package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ForumColumn {
    private Integer id;

    private Integer userId;

    private String name;

    private String coverImgUrl;

    private Integer fansCount;

    private Date addTime;

    private Date updateTime;

}