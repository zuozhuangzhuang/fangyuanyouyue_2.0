package com.fangyuanyouyue.wallet.service;

import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.wallet.dto.UserCouponDto;

import java.math.BigDecimal;
import java.util.List;

public interface UserCouponService {
    /**
     * 获取用户拥有的优惠券
     * @param userId
     * @return
     * @throws ServiceException
     */
    List<UserCouponDto> getListByUserId(Integer userId) throws ServiceException;

    /**
     * 根据优惠券id计算价格
     * @param userId
     * @param price
     * @param couponId
     * @return
     * @throws ServiceException
     */
    BigDecimal getPriceByCoupon(Integer userId,BigDecimal price,Integer couponId) throws ServiceException;
}
