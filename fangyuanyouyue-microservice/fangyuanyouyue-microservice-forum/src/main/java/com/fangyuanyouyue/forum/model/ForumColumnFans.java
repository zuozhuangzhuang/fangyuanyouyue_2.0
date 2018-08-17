package com.fangyuanyouyue.forum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 专栏粉丝
 */
@Getter
@Setter
@ToString
public class ForumColumnFans {
    private Integer id;

    private Integer userId;//用户id

    private Integer columnId;//专栏id

    private Date addTime;//添加时间

}