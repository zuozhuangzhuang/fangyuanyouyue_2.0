package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户反馈
 */
@Getter
@Setter
@ToString
public class Feedback {
    private Integer id;

    private Integer userId;//用户id

    private Date addTime;//新增时间

    private String version;//版本

    private Integer type;//类型 1安卓 2iOS 3小程序

    private String content;//反馈内容

    private String nickName;//昵称
}