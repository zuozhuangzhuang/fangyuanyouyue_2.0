package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户粉丝表
 */
@Getter
@Setter
@ToString
public class UserFans {
    private Integer id;//唯一自增ID

    private Integer userId;//关注人ID

    private Integer toUserId;//被关注人ID

    private Date addTime;//添加时间

}