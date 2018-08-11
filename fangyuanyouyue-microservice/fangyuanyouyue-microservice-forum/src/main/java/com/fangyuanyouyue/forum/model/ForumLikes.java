package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ForumLikes {
    private Integer id;

    private Integer userId;

    private Integer forumId;

    private Date addTime;
    
    private String headImgUrl;
    
    private String nickName;


}