package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.forum.model.BannerIndex;

/**
 * banner
 */
public class BannerIndexDto {

    private Integer bannerId;

    private Integer businessId;

    private Integer jumpType;

    private Integer type;

    private String title;

    private String imgUrl;

    private Integer sort;

    private Integer status;

    public BannerIndexDto() {
    	
    }

    public BannerIndexDto(BannerIndex model) {
        this.bannerId = model.getId();
        this.businessId = model.getBusinessId();
        this.jumpType = model.getJumpType();
        this.type = model.getType();
        this.title = model.getTitle();
        this.imgUrl = model.getImgUrl();
        this.sort = model.getSort();
        this.status = model.getStatus();
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
