package com.fangyuanyouyue.base.enums;

/**
 * 状态值
 */
public enum Status {
    YES(1),NO(2),

    //修改余额、积分、信誉度type 1增加 2减少
    ADD(1),SUB(2),

    //用户行为business_type 对象类型 1用户 2商品、抢购 3商品、抢购评论 4帖子、视频 5帖子、视频评论 6全民鉴定 7全民鉴定评论
    BUSINESS_TYPE_USER(1),BUSINESS_TYPE_GOODS(2),BUSINESS_TYPE_GOODS_COMMENT(3),BUSINESS_TYPE_FORUM(4),
    BUSINESS_TYPE_FORUM_COMMENT(5),BUSINESS_TYPE_APPRAISAL(6),BUSINESS_TYPE_APPRAILSA_COMMENT(7),

    //行为类型 1点赞 2关注用户 3评论 4购买抢购
    BEHAVIOR_TYPE_LIKES(1),BEHAVIOR_TYPE_FANS(2),BEHAVIOR_TYPE_COMMENT(3),BUY_AUCTION(4),

    //1商品 2抢购
    GOODS(1),AUCTION(2),

    //1帖子 2视频
    FORUM(1),VIDEO(2),

    //1好评 2中评 3差评
    EVALUATION_GOOD(1),EVALUATION_NORMAL(2),EVALUATION_BAD(3),

    //支付方式 1微信 2支付宝 3余额 4小程序
    PAY_TYPE_WECHAT(1),PAY_TYPE_ALIPAY(2),PAY_TYPE_BALANCE(3),PAY_TYPE_MINI(4),

    //消息类型 1系统消息 2交易消息 3社交消息 4新增粉丝 5邀请我
    SYSTEM_MESSAGE("1"),SELLER_MESSAGE("2"),SOCIAL_MESSAGE("3"),FANS_MESSAGE("4"),INVITE_MESSAGE("5"),

    /**
     * 跳转类型
     * 1系统消息（不跳转） 2商品详情 3抢购详情 4帖子详情 5视频详情 6全民鉴定详情 7钱包页面 8会员页面
     * 9官方鉴定列表 10买家订单详情 11卖家订单详情 12我的专栏 13实名认证通过 14官方认证通过 15申请专栏通过
     * 16实名认证拒绝 17官方认证拒绝 18申请专栏拒绝
     */
    JUMP_TYPE_SYSTEM("1"),JUMP_TYPE_GOODS("2"),JUMP_TYPE_AUCTION("3"),JUMP_TYPE_FORUM("4"),JUMP_TYPE_VIDEO("5"),JUMP_TYPE_APPRAISAL("6"),JUMP_TYPE_WALLET("7"),
    JUMP_TYPE_VIP("8"),JUMP_TYPE_PLATFORM_APPRAISAL("9"),JUMP_TYPE_ORDER_BUYER("10"),JUMP_TYPE_ORDER_SELLER("11"),JUMP_TYPE_COLUMN("12"),
    JUMP_TYPE_EXT_AGREE("13"),JUMP_TYPE_AUTH_TYPE_AGREE("14"),JUMP_TYPE_COLUMN_AGREE("15"),
    JUMP_TYPE_EXT_REFUSE("16"),JUMP_TYPE_AUTH_TYPE_REFUSE("17"),JUMP_TYPE_COLUMN_REFUSE("18"),


    //会员等级 1铂金会员 2至尊会员
    VIP_LEVEL_LOW(1),VIP_LEVEL_HIGH(2),

    //会员类型 1一个月 2三个月 3一年会员
    VIP_TYPE_ONE_MONTH(1),VIP_TYPE_THREE_MONTH(2),VIP_TYPE_ONE_YEAR(3),

    //会员开通类型 1开通 2续费 3取消
    VIP_DREDGE(1),VIP_RENEW(2),VIP_CANCEL(3),
    //会员状态 1已开通 2未开通
    IS_VIP(1),NOT_VIP(2),

    //商品状态 1出售中 2已售出 3已下架（已结束）5删除


    //(全民鉴定、申请专栏、议价、官方鉴定、充值、开通、续费会员)订单状态 1待支付 2已完成 3已删除
    ORDER_UNPAID(1),ORDER_COMPLETE(2),ORDER_DELETE(3),

    //订单类型 1商品、抢购 2官方鉴定 3商品议价 4全民鉴定 5专栏(申请专栏：支出、每日返利：收入、申请被拒：退款) 6充值 7提现 8开通会员 9续费会员 10认证店铺 11系统修改余额
    GOODS_INFO(1),PLATFORM_APPRAISAL(2),BARGAIN(3),APPRAISAL(4),FORUM_COLUMN(5),RECHARGE(6),WITHDRAW(7),ADD_VIP(8),RENEW_VIP(9),SHOP_AUTH(10),SYSTEM_UPDATE(11),

    //收支类型 1收入 2支出 3退款
    INCOME(1),EXPEND(2),REFUND(3),

    //商品订单、商品支付表状态 1待支付 2待发货 3待收货 4已完成 5已取消
    ORDER_GOODS_PREPAY(1),ORDER_GOODS_PAY(2),ORDER_GOODS_SENDED(3),ORDER_GOODS_COMPLETE(4),ORDER_GOODS_CANCEL(5),

    //议价状态 1申请 2同意 3拒绝 4取消
    BARGAIN_APPLY(1),BARGAIN_AGREE(2),BARGAIN_REFUSE(3),BARGAIN_CANCEL(4),

    //提现状态 1申请中 2同意 3拒绝
    WITHDRAW_APPLY(1,"申请中"),WITHDRAW_AGGRES(2,"同意"),WITHDRAW_REFUSE(3,"拒绝"),

    //代金券 1未使用 2已使用
    COUPON_NOTUSE(1,"未使用"),COUPON_USED(2,"已使用"),

    //用户状态 1正常 2冻结 3删除
    USER_FROZEN(2),USER_DELETE(3),

    //限制余额状态 0限制 1未限制
    IS_CONFINED(0),NOT_CONFINED(1),

    //帖子、视频、评论 状态 1显示 2隐藏
    SHOW(1),HIDE(2),
    //状态 1进行中 2结束 3删除
    BEING(1),END(2),DELETE(3),
    //代理信息
    IS_PROXY(1),NOT_PROXY(2)
    ;
    private Integer value;
    private String message;

    Status() {
    }

    Status(Integer value) {
        this.value = value;
    }

    Status(String message) {
        this.message = message;
    }

    Status(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
