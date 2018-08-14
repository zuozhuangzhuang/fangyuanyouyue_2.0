package com.fangyuanyouyue.forum.model;

import java.util.Date;

public class ForumColumnApply {
    private Integer id;

    private Integer userId;

    private Integer typeId;

    private String coverImgUrl;

    private String columnName;

    private Integer status;

    private String reason;

    private Date updateTime;

    private Date addTime;

    private String name;

    private String identity;

    private String identityImgCover;

    private String identityImgBack;

    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl == null ? null : coverImgUrl.trim();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity == null ? null : identity.trim();
    }

    public String getIdentityImgCover() {
        return identityImgCover;
    }

    public void setIdentityImgCover(String identityImgCover) {
        this.identityImgCover = identityImgCover == null ? null : identityImgCover.trim();
    }

    public String getIdentityImgBack() {
        return identityImgBack;
    }

    public void setIdentityImgBack(String identityImgBack) {
        this.identityImgBack = identityImgBack == null ? null : identityImgBack.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
}