package com.fangyuanyouyue.user.dto;

import com.fangyuanyouyue.user.model.UserInvite;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户邀请码相关信息
 */
@Getter
@Setter
@ToString
public class UserInviteDto {
    //用户邀请码
    private String inviteCode;
    //邀请总人数
    private Integer inviteCount;
    //邀请奖励
    private String inviteAward;

}
