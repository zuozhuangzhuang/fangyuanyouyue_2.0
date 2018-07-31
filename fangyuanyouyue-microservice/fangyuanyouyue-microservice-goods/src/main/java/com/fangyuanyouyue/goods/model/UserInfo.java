package com.fangyuanyouyue.goods.model;

import java.util.Date;

/**
 * 用户信息表
 */
public class UserInfo {
    private Integer id;

    private String phone;//手机号码

    private String email;//电子邮件

    private String address;//用户所在地

    private String loginPwd;//登录密码,MD5小写

    private String nickName;//昵称

    private String headImgUrl;//头像图片地址

    private String bgImgUrl;//背景图片地址

    private Integer gender = 0;//性别，1男 2女 0不确定

    private String signature;//个性签名

    private String contact;//联系电话

    private Integer level;//用户等级

    private String levelDesc;//等级描述

    private Integer regType;//注册来源 1app 2微信小程序

    private Integer regPlatform;//注册平台 1安卓 2IOS 3小程序

    private Integer status;//状态 1正常 2冻结

    private Integer pwdErrCount;//登录密码错误次数

    private Integer payPwdErrCount;//支付密码错误次数

    private Date lastLoginTime;//最后登录时间

    private Integer lastLoginPlatform;//最后登录平台 1安卓 2IOS 3小程序

    private Date addTime;//注册时间

    private Date updateTime;//更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd == null ? null : loginPwd.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl == null ? null : headImgUrl.trim();
    }

    public String getBgImgUrl() {
        return bgImgUrl;
    }

    public void setBgImgUrl(String bgImgUrl) {
        this.bgImgUrl = bgImgUrl == null ? null : bgImgUrl.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc == null ? null : levelDesc.trim();
    }

    public Integer getRegType() {
        return regType;
    }

    public void setRegType(Integer regType) {
        this.regType = regType;
    }

    public Integer getRegPlatform() {
        return regPlatform;
    }

    public void setRegPlatform(Integer regPlatform) {
        this.regPlatform = regPlatform;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPwdErrCount() {
        return pwdErrCount;
    }

    public void setPwdErrCount(Integer pwdErrCount) {
        this.pwdErrCount = pwdErrCount;
    }

    public Integer getPayPwdErrCount() {
        return payPwdErrCount;
    }

    public void setPayPwdErrCount(Integer payPwdErrCount) {
        this.payPwdErrCount = payPwdErrCount;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLastLoginPlatform() {
        return lastLoginPlatform;
    }

    public void setLastLoginPlatform(Integer lastLoginPlatform) {
        this.lastLoginPlatform = lastLoginPlatform;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}