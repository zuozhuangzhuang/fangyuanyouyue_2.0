package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    UserCoupon selectUserCouponDetail(@Param("id") Integer id);

}