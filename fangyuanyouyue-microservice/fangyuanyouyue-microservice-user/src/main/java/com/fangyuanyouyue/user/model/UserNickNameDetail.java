package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户修改昵称记录表
 */
@Getter
@Setter
@ToString
public class UserNickNameDetail {
    private Integer id;

    private Integer userId;//用户id

    private String oldNickName;//修改前昵称

    private String newNickName;//修改后昵称

    private Date addTime;//添加时间

    private String nickName;//用户当前昵称
}