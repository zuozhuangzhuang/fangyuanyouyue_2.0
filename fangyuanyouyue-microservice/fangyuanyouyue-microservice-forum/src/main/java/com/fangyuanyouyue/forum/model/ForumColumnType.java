package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ForumColumnType {
    private Integer id;

    private String name;

    private Integer sort;

    private Date addTime;

    private Date updateTime;
}