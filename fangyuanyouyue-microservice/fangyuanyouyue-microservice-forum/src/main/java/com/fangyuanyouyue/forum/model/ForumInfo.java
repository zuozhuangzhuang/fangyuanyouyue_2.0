package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ForumInfo {
    private Integer id;

    private Integer userId;

    private Integer columnId;
    
    private String title;

    private String videoUrl;

    private Integer videoLength;

    private String label;

    private Integer sort;

    private Integer type;

    private Integer status;

    private Date addTime;

    private Date updateTime;

    private String content;
    
    private String headImgUrl;
    
    private String nickName;


    
}