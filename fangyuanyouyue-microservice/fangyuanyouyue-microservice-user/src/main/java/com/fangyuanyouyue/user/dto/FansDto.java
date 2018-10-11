package com.fangyuanyouyue.user.dto;

import com.fangyuanyouyue.user.model.UserFans;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 粉丝汇总DTO
 */
@Getter
@Setter
@ToString
public class FansDto {
    private List<UserFansDto> userFansDtos;//用户粉丝/关注DTO列表
    private Integer count;//关注/粉丝数
}
