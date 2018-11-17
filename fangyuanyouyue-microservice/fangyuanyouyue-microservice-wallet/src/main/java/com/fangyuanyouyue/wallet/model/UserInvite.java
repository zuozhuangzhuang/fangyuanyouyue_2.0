package com.fangyuanyouyue.wallet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * 用户邀请详情
 */
@Getter
@Setter
@ToString
public class UserInvite {
    private Integer id;

    private Integer userId;//邀请用户id

    private String userInviteCode;//用户邀请码

    private Integer inviteUserId;//被邀请用户id

    private Date addTime;//添加时间

    private Date updateTime;//修改时间
}