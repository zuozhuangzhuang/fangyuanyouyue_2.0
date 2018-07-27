package com.fangyuanyouyue.user.model;

/**
 * 微信小程序相关信息
 */
public class WeChatSession {
    public static final long serialVersionUID = 1L;
    public static final String APPID = "wx69860c142cdc5391";
    public static final String SECRET = "85eb12819eee80ae2ad054825dc7932e";

    private Integer id;
    private String code;//微信获取的code
    private String openid;//微信用户在此小程序的识别号
    private String unionid;//微信用户的固定唯一识别号
    private String session_key;//对用户数据进行加密签名的密钥
    private String encryptedData;//包括敏感数据在内的完整用户信息的加密数据
    private String iv;//加密算法的初始向量
    private Long addTime;
    private Long updateTime;//更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
