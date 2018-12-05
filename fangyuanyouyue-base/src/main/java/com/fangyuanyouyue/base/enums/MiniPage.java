package com.fangyuanyouyue.base.enums;

public enum MiniPage {
    //抢购
    AUCTION_DETAIL(""),
    //商品
    GOODS_DETAIL(""),
    //帖子
    FORUM_DETAIL(""),
    //个人店铺
    SHOP_DETAIL(""),
    ;

    private String url;

    MiniPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
