package com.fangyuanyouyue.user.model;

import java.util.Date;

/**
 * 实名认证申请表
 */
public class IdentityAuthApply {
    private Integer id;//唯一自增ID

    private Integer userId;//用户id，user_info id

    private String name;//姓名

    private String identity;//身份证号码

    private String identityImgCover;//身份证封面图

    private String identityImgBack;//身份证背面

    private String rejectDesc;//拒绝原因

    private Integer status;//状态 1申请 2通过 3拒绝

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

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

    public String getRejectDesc() {
        return rejectDesc;
    }

    public void setRejectDesc(String rejectDesc) {
        this.rejectDesc = rejectDesc == null ? null : rejectDesc.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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