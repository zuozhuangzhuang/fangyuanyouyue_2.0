package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserCoupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCouponMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCoupon record);

    int insertSelective(UserCoupon record);

    UserCoupon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCoupon record);

    int updateByPrimaryKey(UserCoupon record);
}