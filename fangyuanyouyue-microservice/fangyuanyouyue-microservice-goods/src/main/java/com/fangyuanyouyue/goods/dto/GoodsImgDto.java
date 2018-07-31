package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.GoodsImg;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品图片Dto
 */
public class GoodsImgDto {

    private Integer goodsImgId;//图片id

    private Integer goodsId;//商品id

    private String imgUrl;//图片地址

    private Integer type;//类型 1主图（展示在第一张的图片） 2次图

    private Integer sort;//排序

    public GoodsImgDto() {
    }

    public GoodsImgDto(GoodsImg goodsImg) {
        this.goodsImgId = goodsImg.getId();
        this.goodsId = goodsImg.getGoodsId();
        this.imgUrl = goodsImg.getImgUrl();
        this.type = goodsImg.getType();
        this.sort = goodsImg.getSort();
    }

    public static List<GoodsImgDto> toDtoList(List<GoodsImg> list) {
        if (list == null)
            return null;
        List<GoodsImgDto> dtolist = new ArrayList<>();
        for (GoodsImg model : list) {
            GoodsImgDto dto = new GoodsImgDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

    public Integer getGoodsImgId() {
        return goodsImgId;
    }

    public void setGoodsImgId(Integer goodsImgId) {
        this.goodsImgId = goodsImgId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
