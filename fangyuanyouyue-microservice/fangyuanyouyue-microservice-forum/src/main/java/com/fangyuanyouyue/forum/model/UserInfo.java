package com.fangyuanyouyue.forum.model;

import java.util.Date;

public class UserInfo {
    private Integer id;

    private String phone;

    private String email;

    private String address;

    private String loginPwd;

    private String nickName;

    private String headImgUrl;

    private String bgImgUrl;

    private Integer gender;

    private String signature;

    private String contact;

    private Integer level;

    private String levelDesc;

    private Integer regType;

    private Integer regPlatform;

    private Integer status;

    private Integer pwdErrCount;

    private Integer payPwdErrCount;

    private Date lastLoginTime;

    private Integer lastLoginPlatform;

    private Date addTime;

    private Date updateTime;

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