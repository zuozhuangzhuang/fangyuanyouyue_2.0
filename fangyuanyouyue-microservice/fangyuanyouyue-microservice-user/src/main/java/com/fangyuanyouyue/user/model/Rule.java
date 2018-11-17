package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 规则
 */
@Getter
@Setter
@ToString
public class Rule {
    private Integer id;
    //规则内容
    private String ruleContent;
    //规则类型 1邀请规则
    private Integer ruleType;

    private Date addTime;

    private Date updateTime;

}