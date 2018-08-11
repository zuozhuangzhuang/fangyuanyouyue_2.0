package com.fangyuanyouyue.forum.dto;

import java.util.ArrayList;
import java.util.List;

import com.fangyuanyouyue.forum.model.BannerIndex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * banner
 */
@Getter
@Setter
@ToString
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

}
