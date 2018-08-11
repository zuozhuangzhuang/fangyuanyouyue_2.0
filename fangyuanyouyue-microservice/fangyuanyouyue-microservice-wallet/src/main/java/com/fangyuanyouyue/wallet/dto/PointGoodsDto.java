package com.fangyuanyouyue.wallet.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.wallet.model.PointGoods;

public class PointGoodsDto {
	
    private Integer goodsId;

    private String name;//商品名称

    private Long point;//需要的积分

    private String coverImgUrl;//封面图

    private String description;//描述
    

    public PointGoodsDto(PointGoods model) {
        this.goodsId = model.getId();
        this.name = model.getName();
        this.point = model.getPoint();
        this.coverImgUrl = model.getCoverImgUrl();
        this.description = model.getDescription();
    }

    public static List<PointGoodsDto> toDtoList(List<PointGoods> list) {
        if (list == null)
            return null;
        List<PointGoodsDto> dtolist = new ArrayList<>();
        for (PointGoods model : list) {
        	PointGoodsDto dto = new  PointGoodsDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public String getCoverImgUrl() {
		return coverImgUrl;
	}

	public void setCoverImgUrl(String coverImgUrl) {
		this.coverImgUrl = coverImgUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}