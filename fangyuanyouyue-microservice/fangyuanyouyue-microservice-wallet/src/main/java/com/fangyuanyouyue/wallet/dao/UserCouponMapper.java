package com.fangyuanyouyue.wallet.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.wallet.model.UserCoupon;

@Mapper
public interface UserCouponMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCoupon record);

    int insertSelective(UserCoupon record);

    UserCoupon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCoupon record);

    int updateByPrimaryKey(UserCoupon record);
}