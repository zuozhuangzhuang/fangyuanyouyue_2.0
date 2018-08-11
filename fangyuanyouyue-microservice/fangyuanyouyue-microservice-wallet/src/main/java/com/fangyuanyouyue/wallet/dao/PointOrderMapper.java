package com.fangyuanyouyue.wallet.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.wallet.model.PointOrder;

@Mapper
public interface PointOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PointOrder record);

    int insertSelective(PointOrder record);

    PointOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PointOrder record);

    int updateByPrimaryKeyWithBLOBs(PointOrder record);

    int updateByPrimaryKey(PointOrder record);
}