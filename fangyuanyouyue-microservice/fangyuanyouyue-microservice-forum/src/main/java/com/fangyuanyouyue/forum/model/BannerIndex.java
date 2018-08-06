package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class BannerIndex {
    private Integer id;

    private Integer businessId;

    private Integer jumpType;

    private Integer type;

    private String title;

    private String imgUrl;

    private Integer sort;

    private Integer status;

    private Date addTime;

    private Date updateTime;

}