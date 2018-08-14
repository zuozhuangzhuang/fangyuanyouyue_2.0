package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class AppraisalCommentLikes {
    private Integer id;

    private Integer userId;

    private Integer commentId;

    private Date addTime;
}