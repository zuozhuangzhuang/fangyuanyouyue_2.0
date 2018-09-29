package com.fangyuanyouyue.forum.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 鉴定图片表
 */
@Getter
@Setter
@ToString
public class AppraisalImg {
    private Integer id;//唯一自增id

    private String imgUrl;//图片路径

    private Integer appraisalId;//鉴定id

    private Date addTime;//添加时间

}