package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.HotSearch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 热门搜索DTO
 */
@Getter
@Setter
@ToString
public class SearchDto {
    private Integer id;
    private String name;//搜索名称
    private Integer count;//统计数

    public SearchDto() {
    }

    public SearchDto(HotSearch hotSearch) {
        this.id = hotSearch.getId();
        this.name = hotSearch.getName();
        this.count = hotSearch.getCount();
    }

    public static List<SearchDto> toDtoList(List<HotSearch> list) {
        if (list == null)
            return null;
        List<SearchDto> dtolist = new ArrayList<>();
        for (HotSearch model : list) {
            SearchDto dto = new SearchDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }

}
