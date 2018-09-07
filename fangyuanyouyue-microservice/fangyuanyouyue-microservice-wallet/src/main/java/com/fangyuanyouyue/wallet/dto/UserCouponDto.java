package com.fangyuanyouyue.wallet.dto;

import com.fangyuanyouyue.base.util.DateUtil;
import com.fangyuanyouyue.wallet.model.UserCoupon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户优惠券Dto
 */
@Getter
@Setter
@ToString
public class UserCouponDto {
    private Integer id;

    private Integer userId;//拥有者id

    private Integer couponId;//优惠券ID

    private Integer status;//状态 1未使用 2已使用 3已过期

    private String addTime;//添加时间

    private String updateTime;//修改时间

    private BigDecimal couponAmount;//优惠券金额

    private Integer couponType;//优惠券类型 1满减 2折扣

    private BigDecimal conditionAmount;//满减达到的条件

    private String startTime;//有效开始时间

    private String endTime;//过期时间

    public UserCouponDto() {
    }

    public UserCouponDto(UserCoupon userCoupon) {
        this.id = userCoupon.getId();
        this.userId = userCoupon.getUserId();
        this.couponId = userCoupon.getCouponId();
        this.status = userCoupon.getStatus();
        this.addTime = DateUtil.getFormatDate(userCoupon.getAddTime(),DateUtil.DATE_FORMT);
        this.updateTime = DateUtil.getFormatDate(userCoupon.getUpdateTime(),DateUtil.DATE_FORMT);
        this.couponAmount = userCoupon.getCouponAmount();
        this.couponType = userCoupon.getCouponType();
        this.conditionAmount = userCoupon.getConditionAmount();
        this.startTime = DateUtil.getFormatDate(userCoupon.getStartTime(),DateUtil.DATE_FORMT);
        this.endTime = DateUtil.getFormatDate(userCoupon.getEndTime(),DateUtil.DATE_FORMT);
    }

    public static List<UserCouponDto> toDtoList(List<UserCoupon> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<UserCouponDto> dtolist = new ArrayList<>();
        for (UserCoupon model : list) {
            UserCouponDto dto = new UserCouponDto(model);
            dtolist.add(dto);
        }
        return dtolist;
    }
}
