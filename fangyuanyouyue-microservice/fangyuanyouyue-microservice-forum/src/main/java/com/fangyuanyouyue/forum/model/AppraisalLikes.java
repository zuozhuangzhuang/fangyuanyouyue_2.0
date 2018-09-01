package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 鉴定点赞
 */
@Getter
@Setter
@ToString
public class AppraisalLikes {
    private Integer id;

    private Integer userId;//点赞用户id

    private Integer appraisalId;//鉴定id

    private Date addTime;//添加时间
}