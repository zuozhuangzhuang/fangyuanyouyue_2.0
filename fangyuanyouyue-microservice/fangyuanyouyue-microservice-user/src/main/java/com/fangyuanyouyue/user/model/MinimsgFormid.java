package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户发送模板消息所需表单id
 */
@Getter
@Setter
@ToString
public class MinimsgFormid {
    private Integer id;

    private Integer userId;//用户id

    private String formId;//小程序表单提交id

    private Date addTime;//添加时间

    private Date updateTime;//修改时间
}