package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.BannerIndex;
import com.fangyuanyouyue.goods.model.GoodsCorrelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 轮播图dto
 */
public class BannerIndexDto {
    private Integer bannerId;//唯一自增ID

    private Integer businessId;//业务ID:商品ID/用户ID

    private Integer jumpType;//跳转类型,0:商品 1：个人

    private Integer type;//业务类型,0:商品 1：个人

    private String title;//描述标题

    private String imgUrl;//图片地址

    private Integer sort;//排序，由低到高

    private Integer status;//是否下架，0未下架 1下架

    public BannerIndexDto() {
    }

    public BannerIndexDto(BannerIndex bannerIndex) {
        this.bannerId = bannerIndex.getId();
        this.businessId = bannerIndex.getBusinessId();
        this.jumpType = bannerIndex.getJumpType();
        this.type = bannerIndex.getType();
        this.title = bannerIndex.getTitle();
        this.imgUrl = bannerIndex.getImgUrl();
        this.sort = bannerIndex.getSort();
        this.status = bannerIndex.getStatus();
    }

    public static List<BannerIndexDto> toDtoList(List<BannerIndex> list) {
        if (list == null)
            return null;
        List<BannerIndexDto> dtolist = new ArrayList<>();
        for (BannerIndex model : list) {
            BannerIndexDto dto = new BannerIndexDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getJumpType() {
        return jumpType;
    }

    public void setJumpType(Integer jumpType) {
        this.jumpType = jumpType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
