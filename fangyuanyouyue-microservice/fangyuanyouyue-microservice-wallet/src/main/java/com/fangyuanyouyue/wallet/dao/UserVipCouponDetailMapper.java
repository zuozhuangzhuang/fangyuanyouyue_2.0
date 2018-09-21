package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserVipCouponDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface UserVipCouponDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVipCouponDetail record);

    int insertSelective(UserVipCouponDetail record);

    UserVipCouponDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVipCouponDetail record);

    int updateByPrimaryKey(UserVipCouponDetail record);

    /**
     * 根据用户id获取优惠券方法数量
     * @param userId
     * @return
     */
    int selectDetailByUserId(@Param("userId")Integer userId, @Param("startTime")Date startTime);
}