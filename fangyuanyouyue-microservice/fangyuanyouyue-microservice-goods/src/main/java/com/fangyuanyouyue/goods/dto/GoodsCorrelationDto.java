package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.GoodsCorrelation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
/**
 * 商品分类关联表Dto
 */
@Getter
@Setter
@ToString
public class GoodsCorrelationDto {
    private Integer correlationId;//唯一自增ID

    private Integer goodsCategoryId;//商品分类ID

    private String goodsCategoryName;//类目名称

    private Integer goodsId;//商品ID

    private Integer categoryParentId;//商品分类父ID

    public GoodsCorrelationDto() {
    }

    public GoodsCorrelationDto(GoodsCorrelation goodsCorrelation) {
        this.correlationId = goodsCorrelation.getId();
        this.goodsCategoryId = goodsCorrelation.getGoodsCategoryId();
        this.goodsCategoryName = goodsCorrelation.getGoodsCategoryName();
        this.goodsId = goodsCorrelation.getGoodsId();
        this.categoryParentId = goodsCorrelation.getCategoryParentId();
    }
    public static List<GoodsCorrelationDto> toDtoList(List<GoodsCorrelation> list) {
        if (list == null) {
            return null;
        }
        List<GoodsCorrelationDto> dtolist = new ArrayList<>();
        for (GoodsCorrelation model : list) {
            GoodsCorrelationDto dto = new GoodsCorrelationDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
