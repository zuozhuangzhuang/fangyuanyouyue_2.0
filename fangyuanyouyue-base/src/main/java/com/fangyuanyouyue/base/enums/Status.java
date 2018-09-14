package com.fangyuanyouyue.base.enums;

/**
 * 这里是 12 1234567 123 12 123
 */
public enum Status {
    //修改余额、积分、信誉度type 1增加 2减少
    ADD(1),SUB(2),
    //用户行为business_type 对象类型 1用户 2商品、抢购 3商品、抢购评论 4帖子、视频 5帖子、视频评论 6全民鉴定 7全民鉴定评论
    BUSINESS_TYPE_USER(1),BUSINESS_TYPE_GOODS(2),BUSINESS_TYPE_GOODS_COMMENT(3),BUSINESS_TYPE_FORUM(4),
    BUSINESS_TYPE_FORUM_COMMENT(5),BUSINESS_TYPE_APPRAISAL(6),BUSINESS_TYPE_APPRAILSA_COMMENT(7),
    //行为类型 1点赞 2关注用户 3评论
    BEHAVIOR_TYPE_LIKES(1),BEHAVIOR_TYPE_FANS(2),BEHAVIOR_TYPE_COMMENT(3),
    //1商品 2抢购
    GOODS(1),AUCTION(2),
    //1好评 2中评 3差评
    EVALUATION_GOOD(1),EVALUATION_NORMAL(2),EVALUATION_BAD(3),
    //支付方式 1微信 2支付宝 3余额 4小程序
    PAY_TYPE_WECHAT(1),PAY_TYPE_ALIPAY(2),PAY_TYPE_BALANCE(3),PAY_TYPE_MINI(4),
    /**
     * 跳转类型
     * 1系统消息 2商品消息 3订单消息 4视频消息 5帖子消息 6专栏消息 7全民鉴定消息 8商品、抢购评论消息
     * 9帖子评论消息 10视频评论消息 11全民鉴定评论消息 12会员特权 13钱包余额
     */

    //会员等级 1铂金会员 2至尊会员
    VIP_LEVEL_LOW(1),VIP_LEVEL_HIGH(2),
    //会员类型 1一个月 2三个月 3一年会员
    VIP_TYPE_ONE_MONTH(1),VIP_TYPE_THREE_MONTH(2),VIP_TYPE_ONE_YEAR(3),
    //会员状态 1已开通 2未开通
    IS_VIP(1),NOT_VIP(2),
    ;
    private final Integer value;

    Status(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
