package com.fangyuanyouyue.base.enums;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 信誉度规则
 */
@Getter
@ToString
public enum Credit {
    //粉丝+2
    FANS(2L),
    //被赞+2
    LIKES(2L),
    //被评论+2
    COMMENT(2L),
    //举报核实+20
    REPORT_VERIFY(20l),
    //被举报核实-40
    REPORT_VERIFYED(40L),
    //被官方删假帖-20
    DELETE_FAKE(20L),
    //被退货-40
    RETURN_ORDER(40L),
    //24h联系不上被举报-20
    MISSING(20L),
    //退货超时不处理-40
    RETURN_TIMEOUT(40L),
    //上传商品+5
    ADD_GOODSINFO(5L),
    //上传抢购+10
    ADD_AUCTION(10L),
    //上传帖子+5
    ADD_FORUM(5L),
    //上传视频+10
    ADD_VIDEO(10L),
    //成交2000以下订单+20
    NORMAL_ORDER(20L),
    //成交2000以上订单+50
    BIG_ORDER(50L),
    //申请实名认证+100
    EXTAPPLY(100L),
    //好评+500
    EVALUATION_GOOD(500L),
    //中评+300
    EVALUATION_NORMAL(300L),
    //差评-300
    EVALUATION_BAD(300L)
    ;

    private final Long credit;

    Credit(Long credit) {
        this.credit = credit;
    }


}
