package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.GoodsQuickSearch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品快速搜索条件表DTO
 */
@Getter
@Setter
@ToString
public class GoodsQuickSearchDto {
    private Integer id;//唯一自增ID

    private BigDecimal priceMin;//最小价格

    private BigDecimal priceMax;//最大价格

    private Integer sort;//显示顺序

    private Integer searchCount;//搜索次数

    private Integer status;//状态 1显示 2不显示

    public GoodsQuickSearchDto() {
    }

    public GoodsQuickSearchDto(GoodsQuickSearch goodsQuickSearch) {
        this.id = goodsQuickSearch.getId();
        this.priceMin = goodsQuickSearch.getPriceMin();
        this.priceMax = goodsQuickSearch.getPriceMax();
        this.sort = goodsQuickSearch.getSort();
        this.searchCount = goodsQuickSearch.getSearchCount();
        this.status = goodsQuickSearch.getStatus();
    }

    public static List<GoodsQuickSearchDto> toDtoList(List<GoodsQuickSearch> list) {
        if (list == null) {
            return null;
        }
        List<GoodsQuickSearchDto> dtolist = new ArrayList<>();
        for (GoodsQuickSearch model : list) {
            GoodsQuickSearchDto dto = new GoodsQuickSearchDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
