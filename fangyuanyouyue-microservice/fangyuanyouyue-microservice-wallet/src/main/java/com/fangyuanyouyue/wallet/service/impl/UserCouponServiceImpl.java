package com.fangyuanyouyue.wallet.service.impl;

import com.codingapi.tx.annotation.TxTransaction;
import com.fangyuanyouyue.base.exception.ServiceException;
import com.fangyuanyouyue.base.util.DateStampUtils;
import com.fangyuanyouyue.wallet.constant.StatusEnum;
import com.fangyuanyouyue.wallet.dao.UserCouponMapper;
import com.fangyuanyouyue.wallet.dto.UserCouponDto;
import com.fangyuanyouyue.wallet.model.UserCoupon;
import com.fangyuanyouyue.wallet.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service(value = "userCouponService")
@Transactional(rollbackFor=Exception.class)
public class UserCouponServiceImpl implements UserCouponService {
    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    public List<UserCouponDto> getListByUserId(Integer userId) throws ServiceException {
        List<UserCoupon> listByUserId = userCouponMapper.getListByUserId(userId);
        List<UserCouponDto> userCouponDtos = UserCouponDto.toDtoList(listByUserId);
        return userCouponDtos;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    @TxTransaction
    public BigDecimal getPriceByCoupon(Integer userId,BigDecimal price, Integer userCouponId) throws ServiceException {
        //根据用户id，优惠券id查询状态为 未使用 的优惠券
        UserCoupon userCoupon = userCouponMapper.selectUserCouponDetail(userId,userCouponId);
        if(userCoupon == null){
            throw new ServiceException("未找到优惠券！");
        }
        BigDecimal couponPrice = price;
        if(userCoupon.getEndTime().getTime() < System.currentTimeMillis()){
            //优惠券已失效
        }else{
            //计算优惠后价格并返回
            if(userCoupon.getCouponType() == 1){
                //满减
                if(price.compareTo(userCoupon.getConditionAmount()) >= 0){
                    //满足满减条件
                    couponPrice = couponPrice.subtract(userCoupon.getCouponAmount());
                    //已使用
                    userCoupon.setStatus(2);
                }
            }else{
                //折扣
            }
        }
        userCouponMapper.updateByPrimaryKeySelective(userCoupon);
        return couponPrice;
    }

    @Override
    public void insertUserCoupon(Integer userId, Integer couponId) throws ServiceException {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setAddTime(DateStampUtils.getTimesteamp());
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(StatusEnum.COUPON_NOTUSE.getCode());
        userCoupon.setUserId(userId);
        userCouponMapper.insert(userCoupon);
    }
}
