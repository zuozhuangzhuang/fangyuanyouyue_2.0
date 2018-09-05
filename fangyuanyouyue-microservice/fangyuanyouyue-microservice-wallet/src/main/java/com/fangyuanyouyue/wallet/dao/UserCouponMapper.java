package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.CouponInfo;
import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.wallet.model.UserCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserCouponMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCoupon record);

    int insertSelective(UserCoupon record);

    UserCoupon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCoupon record);

    int updateByPrimaryKey(UserCoupon record);

    /**
     * 获取用户优惠券详细信息
     * @param userId
     * @param id
     * @return
     */
    UserCoupon selectUserCouponDetail(@Param("userId")Integer userId,@Param("id") Integer id);

    /**
     * 获取用户的优惠券列表
     * @param userId
     * @return
     */
    List<UserCoupon> getListByUserId(@Param("userId")Integer userId);
}