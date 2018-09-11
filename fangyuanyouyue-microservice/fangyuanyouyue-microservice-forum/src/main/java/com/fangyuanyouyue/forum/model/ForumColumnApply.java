package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 专栏申请表
 */
@Getter
@Setter
@ToString
public class ForumColumnApply {
    private Integer id;

    private Integer userId;//申请用户id

    private Integer typeId;//专栏分类id

    private String columnName;//专栏名

    private Integer status;//状态 0申请中 1通过 2未通过

    private String reason;//拒绝理由

    private Date updateTime;//修改时间

    private Date addTime;//添加时间
    
    private String nickName;
    
    private String headImgUrl;
    
    private String phone;
}