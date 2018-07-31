package com.fangyuanyouyue.goods.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品鉴定详情表
 */
public class GoodsAppraisalDetail {
    private Integer id;//唯一自增ID

    private Integer appraisalId;//鉴定id

    private Integer goodsId;//商品id

    private String opinion;//鉴定观点

    private Integer status;//状态 0申请 1真 2假 3存疑

    private String title;//鉴定标题

    private BigDecimal price;//鉴定赏金

    private String description;//描述

    private Date submitTime;//审核时间

    private Date addTime;//添加时间

    private Date updateTime;//更新时间

    private Integer type;//鉴定类型 1商家鉴定 2买家 3普通用户

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppraisalId() {
        return appraisalId;
    }

    public void setAppraisalId(Integer appraisalId) {
        this.appraisalId = appraisalId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion == null ? null : opinion.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}