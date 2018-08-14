package com.fangyuanyouyue.goods.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 鉴定图片表
 */
@Getter
@Setter
@ToString
public class AppraisalUrl {
    private Integer id;//唯一自增ID

    private Integer appraisalId;//鉴定详情id

    private String url;//图片/视频地址

    private Integer type;//类型 1图片 2视频 3视频截图

    private Date addTime;//添加时间
}