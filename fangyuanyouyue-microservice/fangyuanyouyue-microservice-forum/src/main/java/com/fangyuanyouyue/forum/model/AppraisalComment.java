package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class AppraisalComment {
    private Integer id;

    private Integer userId;

    private Integer appraisalId;

    private Integer viewpoint;

    private String content;

    private String pic1;

    private String pic2;

    private String pic3;

    private String pic4;

    private String pic5;

    private String pic6;

    private Integer isWinner;

    private Integer status;

    private Date addTime;

    private Date updateTime;
}