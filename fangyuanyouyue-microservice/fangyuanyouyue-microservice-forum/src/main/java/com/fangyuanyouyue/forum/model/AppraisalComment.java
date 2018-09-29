package com.fangyuanyouyue.forum.model;

import java.sql.Timestamp;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 鉴定评论
 */
@Getter
@Setter
@ToString
public class AppraisalComment {
    private Integer id;//鉴定评论id

    private Integer userId;//评论用户id

    private Integer appraisalId;//鉴定id

    private Integer viewpoint;//评论观点 1看真 2看假

    private String content;//评论内容

    private String pic1;//图片1

    private String pic2;

    private String pic3;

    private String pic4;

    private String pic5;

    private String pic6;

    private Integer isWinner;//观点是否获胜 1是 2否

    private Integer status;//状态

    private Timestamp addTime;//添加时间

    private Date updateTime;//修改时间

    private String nickName;//昵称

    private String headImgUrl;//头像
}