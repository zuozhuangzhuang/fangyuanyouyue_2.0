package com.fangyuanyouyue.forum.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 鉴定浏览记录
 */
@Getter
@Setter
@ToString
public class AppraisalPv {
    private Integer id;

    private Integer userId;//用户id

    private Integer appraisalId;//鉴定id

    private Date addTime;//添加时间
}