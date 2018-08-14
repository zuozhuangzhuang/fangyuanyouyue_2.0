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
    private Integer id;//

    private Integer userId;//用户id

    private String title;//标题

    private BigDecimal bonus;//赏金

    private String label;//标签

    private Integer sort;//排序

    private Integer status;//

    private Date endTime;//结束时间

    private Date updateTime;//修改时间

    private Date addTime;//添加时间

    private String content;//内容描述
}