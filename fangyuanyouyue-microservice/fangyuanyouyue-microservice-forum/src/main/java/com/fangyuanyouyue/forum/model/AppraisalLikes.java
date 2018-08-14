package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class AppraisalLikes {
    private Integer id;

    private Integer userId;

    private Integer appraisalId;

    private Date addTime;
}