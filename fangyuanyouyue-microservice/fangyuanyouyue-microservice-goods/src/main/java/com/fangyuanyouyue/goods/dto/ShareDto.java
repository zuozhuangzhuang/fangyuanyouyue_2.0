package com.fangyuanyouyue.goods.dto;

import com.fangyuanyouyue.goods.model.GoodsInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 分享个人店铺数据dto
 */
@Setter
@Getter
@ToString
public class ShareDto {
    private Integer goodsCount;//商品总数

    private List<GoodsDto> goodsList;//商品列表

    private String imgUrl;//默认图片路径

    private String rule;//分享页面规则图片
}
