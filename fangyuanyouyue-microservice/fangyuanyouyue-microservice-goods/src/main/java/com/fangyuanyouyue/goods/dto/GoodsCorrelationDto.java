package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.GoodsCategory;
import com.fangyuanyouyue.goods.model.GoodsCorrelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*



 */
/**
 * 商品分类关联表Dto
 */
public class GoodsCorrelationDto {
    private Integer correlationId;//唯一自增ID

    private Integer goodsCategoryId;//商品分类ID

    private Integer goodsId;//商品ID

    private Integer categoryParentId;//商品分类父ID

    public GoodsCorrelationDto() {
    }

    public GoodsCorrelationDto(GoodsCorrelation goodsCorrelation) {
        this.correlationId = goodsCorrelation.getId();
        this.goodsCategoryId = goodsCorrelation.getGoodsCategoryId();
        this.goodsId = goodsCorrelation.getGoodsId();
        this.categoryParentId = goodsCorrelation.getCategoryParentId();
    }
    public static List<GoodsCorrelationDto> toDtoList(List<GoodsCorrelation> list) {
        if (list == null)
            return null;
        List<GoodsCorrelationDto> dtolist = new ArrayList<>();
        for (GoodsCorrelation model : list) {
            GoodsCorrelationDto dto = new GoodsCorrelationDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
    public Integer getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(Integer correlationId) {
        this.correlationId = correlationId;
    }

    public Integer getGoodsCategoryId() {
        return goodsCategoryId;
    }

    public void setGoodsCategoryId(Integer goodsCategoryId) {
        this.goodsCategoryId = goodsCategoryId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getCategoryParentId() {
        return categoryParentId;
    }

    public void setCategoryParentId(Integer categoryParentId) {
        this.categoryParentId = categoryParentId;
    }
}
