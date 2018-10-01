package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 系统消息记录表
 */
@Getter
@Setter
@ToString
public class SysMsgLog {
    private Integer id;

    private Integer userId;//发送人id

    private Date addTime;//添加时间

    private String content;//消息体

    private String userName;//姓名
}