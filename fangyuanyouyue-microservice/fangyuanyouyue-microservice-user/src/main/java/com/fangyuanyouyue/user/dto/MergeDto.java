package com.fangyuanyouyue.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 合并账号展示信息DTO
 */
@Getter
@Setter
@ToString
public class MergeDto {
    private Integer fromUserId;//发起用户id

    private String fromUserNickName;//发起用户昵称

    private String fromUserHeadImgUrl;//发起用户头像

    private Integer toUserId;//主用户id

    private String toUserNickName;//主用户昵称

    private String toUserHeadImgUrl;//主用户头像
}
