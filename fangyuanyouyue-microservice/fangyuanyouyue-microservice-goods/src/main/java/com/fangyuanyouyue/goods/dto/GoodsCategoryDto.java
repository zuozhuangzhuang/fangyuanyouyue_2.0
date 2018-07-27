package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.GoodsCategory;
import com.fangyuanyouyue.goods.model.GoodsImg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品分类Dto
 */
public class GoodsCategoryDto {
    private Integer categoryId;//分类ID

    private Integer parentId;//上级id

    private String name;//类目名称

    private String imgUrl;//图片地址

    private Integer sort;//排序

    private Integer type;//类型 1普通 2热门

    private Integer status;//状态 0正常 1禁用

    private List<GoodsCategoryDto> childList;//二级分类

    public GoodsCategoryDto() {
    }

    public GoodsCategoryDto(GoodsCategory goodsCategory) {
        this.categoryId = goodsCategory.getId();
        this.parentId = goodsCategory.getParentId();
        this.name = goodsCategory.getName();
        this.imgUrl = goodsCategory.getImgUrl();
        this.sort = goodsCategory.getSort();
        this.type = goodsCategory.getType();
        this.status = goodsCategory.getStatus();
    }

    public static List<GoodsCategoryDto> toDtoList(List<GoodsCategory> list) {
        if (list == null)
            return null;
        List<GoodsCategoryDto> dtolist = new ArrayList<>();
        for (GoodsCategory model : list) {
            GoodsCategoryDto dto = new GoodsCategoryDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<GoodsCategoryDto> getChildList() {
        return childList;
    }

    public void setChildList(List<GoodsCategoryDto> childList) {
        this.childList = childList;
    }
}
