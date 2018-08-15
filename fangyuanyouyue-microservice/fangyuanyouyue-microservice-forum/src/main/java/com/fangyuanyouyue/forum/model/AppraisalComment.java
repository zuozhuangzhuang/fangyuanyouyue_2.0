package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class AppraisalComment {
    private Integer id;//鉴定评论id

    private Integer userId;//评论用户id

    private Integer appraisalId;//鉴定id

    private Integer viewpoint;//评论观点

    private String content;//评论内容

    private String pic1;//图片1

    private String pic2;

    private String pic3;

    private String pic4;

    private String pic5;

    private String pic6;

    private Integer isWinner;//观点是否获胜

    private Integer status;//状态

    private Date addTime;//添加时间

    private Date updateTime;//修改时间
}