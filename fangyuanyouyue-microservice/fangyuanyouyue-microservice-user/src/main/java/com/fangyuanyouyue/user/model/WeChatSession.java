package com.fangyuanyouyue.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信小程序相关信息
 */
@Getter
@Setter
@ToString
public class WeChatSession {
    public static final long serialVersionUID = 1L;
    public static final String APPID = "wx69860c142cdc5391";
    public static final String SECRET = "b292058a4e2cb4df763b46fe8ece6aae";

    private Integer id;
    private String code;//微信获取的code
    private String openid;//微信用户在此小程序的识别号
    private String unionid;//微信用户的固定唯一识别号
    private String session_key;//对用户数据进行加密签名的密钥
    private String encryptedData;//包括敏感数据在内的完整用户信息的加密数据
    private String iv;//加密算法的初始向量
    private Long addTime;
    private Long updateTime;//更新时间

}
