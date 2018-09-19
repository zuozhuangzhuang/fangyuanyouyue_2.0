package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.CouponInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponInfo record);

    int insertSelective(CouponInfo record);

    CouponInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponInfo record);

    int updateByPrimaryKey(CouponInfo record);
}