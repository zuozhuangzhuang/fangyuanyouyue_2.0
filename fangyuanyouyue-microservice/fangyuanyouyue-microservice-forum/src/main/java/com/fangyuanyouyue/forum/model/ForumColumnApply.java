package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 *
 */
@Getter
@Setter
@ToString
public class ForumColumnApply {
    private Integer id;

    private Integer userId;

    private Integer typeId;

    private String coverImgUrl;

    private String columnName;

    private Integer status;

    private String reason;

    private Date updateTime;

    private Date addTime;

    private String name;

    private String identity;

    private String identityImgCover;

    private String identityImgBack;

    private String phone;
}