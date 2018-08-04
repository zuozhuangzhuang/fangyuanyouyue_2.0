package com.fangyuanyouyue.order.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品详情表
 */
public class GoodsInfo {
    private Integer id;

    private Integer userId;//发布用户id

    private String name;//商品名称

    private String description;//商品详情

    private BigDecimal price;//商品价格

    private BigDecimal postage;//运费

    private Integer sort;//排序

    private String label;//标签

    private Integer type;//类型 1普通商品 2抢购商品

    private Integer status;//状态 1出售中 2已售出 5删除

    private BigDecimal floorPrice;//最低价

    private Integer intervalTime;//降价时间间隔(单位秒)

    private BigDecimal markdown;//降价幅度

    private Date lastIntervalTime;//最后一次降价时间

    private Integer isAppraisal;//是否官方鉴定 1未鉴定 2已鉴定

    private String videoUrl;//视频路径

    private Date addTime;//发布时间

    private Date updateTime;//修改时间

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(BigDecimal floorPrice) {
        this.floorPrice = floorPrice;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public BigDecimal getMarkdown() {
        return markdown;
    }

    public void setMarkdown(BigDecimal markdown) {
        this.markdown = markdown;
    }

    public Date getLastIntervalTime() {
        return lastIntervalTime;
    }

    public void setLastIntervalTime(Date lastIntervalTime) {
        this.lastIntervalTime = lastIntervalTime;
    }

    public Integer getIsAppraisal() {
        return isAppraisal;
    }

    public void setIsAppraisal(Integer isAppraisal) {
        this.isAppraisal = isAppraisal;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl == null ? null : videoUrl.trim();
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