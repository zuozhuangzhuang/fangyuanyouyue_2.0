package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 用户邀请码
 */
@Getter
@Setter
@ToString
public class InviteCode {
    private Integer id;

    private Integer userId;//用户id

    private String userCode;//用户识别码（邀请码、代理码）

    private Date addTime;//添加时间

    private Date updateTime;//修改时间
}