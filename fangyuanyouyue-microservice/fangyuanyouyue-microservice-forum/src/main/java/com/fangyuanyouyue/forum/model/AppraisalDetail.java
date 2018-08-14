package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class AppraisalDetail {
    private Integer id;

    private Integer userId;

    private String title;

    private BigDecimal bonus;

    private String label;

    private Integer sort;

    private Integer status;

    private Date endTime;

    private Date updateTime;

    private Date addTime;

    private String content;
}