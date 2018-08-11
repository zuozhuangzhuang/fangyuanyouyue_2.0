package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户第三方登录信息表
 */
@Getter
@Setter
@ToString
public class UserThirdParty {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id，user_info id

    private Integer type;//类型 1微信 2QQ 3微博

    private String unionId;//第三方唯一ID

    private String appOpenId;//微信app openid

    private String mpOpenId;//公众号openid

    private String miniOpenId;//小程序openid

    private String sessionKey;//对用户数据进行加密签名的密钥

    private String nickName;//第三方账号昵称

    private String headImgUrl;//第三方账号头像地址

    private Date addTime;//添加时间

    private Date updateTime;//更新时间


}