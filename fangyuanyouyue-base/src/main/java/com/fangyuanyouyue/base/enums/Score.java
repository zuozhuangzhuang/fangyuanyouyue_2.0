package com.fangyuanyouyue.base.enums;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 用户积分规则
 */
public enum  Score {
    //所有点赞+5
    LIKES(5L),
    //所有评论+8
    COMMENT(8L),
    //分享+10
    SHARE(10L),
    //发布商品、抢购+20
    ADD_GOODSINFO(20L),
    //发布帖子、视频+100
    ADD_FORUMINFO(100L)


    ;

    private final Long score;

    Score(Long score) {
        this.score = score;
    }

    public Long getScore() {
        return score;
    }

    /**
     * 成交订单+成交价10%
     * @param price
     */
    public static Long getScore(BigDecimal price) {
        return price.multiply(new BigDecimal(0.1)).longValue();
    }
}
