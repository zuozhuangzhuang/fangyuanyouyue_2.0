package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.BannerIndex;
import com.fangyuanyouyue.goods.model.GoodsCorrelation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 轮播图dto
 */
@Getter
@Setter
@ToString
public class BannerIndexDto {
    private Integer bannerId;//唯一自增ID

    private Integer businessId;//业务ID：例：商品id、视频id、积分商城商品id...

    private Integer jumpType;//跳转类型 1页面 2链接 3图片（businessId为空）

    private Integer businessType;//业务类型 1商品详情、2抢购详情、3帖子详情、4全民鉴定详情、5视频详情、6专栏 7积分商品

    private Integer type;//类型 1首页主页 2商品详情 3积分商城 4首页专栏

    private String title;//描述标题

    private String imgUrl;//图片地址

    private Integer sort;//排序，由低到高

    private Integer status;//是否展示，1展示 2不展示

    public BannerIndexDto() {
    }

    public BannerIndexDto(BannerIndex bannerIndex) {
        this.bannerId = bannerIndex.getId();
        this.businessId = bannerIndex.getBusinessId();
        this.jumpType = bannerIndex.getJumpType();
        this.businessType = bannerIndex.getBusinessType();
        this.type = bannerIndex.getType();
        this.title = bannerIndex.getTitle();
        this.imgUrl = bannerIndex.getImgUrl();
        this.sort = bannerIndex.getSort();
        this.status = bannerIndex.getStatus();
    }

    public static List<BannerIndexDto> toDtoList(List<BannerIndex> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<BannerIndexDto> dtolist = new ArrayList<>();
        for (BannerIndex model : list) {
            BannerIndexDto dto = new BannerIndexDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
