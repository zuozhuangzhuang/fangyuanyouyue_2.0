package com.fangyuanyouyue.base.enums;

import com.alibaba.fastjson.JSONObject;

/**
 * 小程序二维码跳转页面
 */
public enum MiniPage {
    //抢购
    AUCTION_DETAIL(1,"page/market/pages/For_the_details/For_the_details"),
    //商品
    GOODS_DETAIL(2,"page/market/pages/goods_details/goods_details"),
    //帖子
    FORUM_DETAIL(3,"page/index/pages/details_columns/details_columns"),
    //个人店铺
    SHOP_DETAIL(4,"page/market/pages/store/store"),
    ;

    //类型 1抢购 2商品 3帖子 4个人店铺
    private Integer type;
    private String url;

    MiniPage() {
    }

    MiniPage(Integer type, String urle) {
        this.type = type;
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
