package com.fangyuanyouyue.wallet.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.wallet.model.CouponInfo;
@Mapper
public interface CouponInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponInfo record);

    int insertSelective(CouponInfo record);

    CouponInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponInfo record);

    int updateByPrimaryKey(CouponInfo record);
}