package com.fangyuanyouyue.base.enums;

/**
 * 支付回调接口
 */
public enum NotifyUrl {
//    开发环境
    dev_notify("http://localhost:8769"),
//    测试环境
    test_notify("http://zuul.fangyuanyouyue.com:80"),
//    生产环境
    prod_notify(""),

//    微信支付回调接口
//    商品议价
    bargain_wechat_notify("/goods/bargain/notify/wechat"),
//    官方鉴定
    appraisal_wechat_notify("/goods/appraisal/notify/wechat"),
//    商品订单
    order_wechat_notify("/order/order/notify/wechat"),
//    全民鉴定
    argue_wechat_notify("/forum/appraisal/notify/wechat"),
//    专栏
    column_wechat_notify("/forum/column/notify/wechat"),
//    会员订单
    vip_wechat_notify("/wallet/vip/notify/wechat"),
//    充值订单
recharge_wechat_notify("/wallet/wallet/notify/wechat"),

//    支付宝回调接口
//    商品议价
    bargain_alipay_notify("/goods/bargain/notify/alipay"),
//    官方鉴定
    appraisal_alipay_notify("/goods/appraisal/notify/alipay"),
//    商品订单
    order_alipay_notify("/order/order/notify/alipay"),
//    全民鉴定
    argue_alipay_notify("/forum/appraisal/notify/alipay"),
//    专栏
    column_alipay_notify("/forum/column/notify/alipay"),
//    会员订单
    vip_alipay_notify("/wallet/vip/notify/alipay"),
    //    充值订单
    recharge_alipay_notify("/wallet/wallet/notify/alipay");

    private final String notifUrl;

    NotifyUrl(String notifUrl) {
        this.notifUrl = notifUrl;
    }

    public String getNotifUrl() {
        return notifUrl;
    }
}
