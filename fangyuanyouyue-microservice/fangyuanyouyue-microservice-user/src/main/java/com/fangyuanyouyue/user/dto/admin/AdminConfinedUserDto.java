package com.fangyuanyouyue.user.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 代理用户信息
 */
@Getter
@Setter
@ToString
public class AdminConfinedUserDto {
    private AdminUserDto adminUserDto;
    private Integer goodsCount;
    private Integer parentId;
    private String nickName;
    private String phone;//手机号码
}
