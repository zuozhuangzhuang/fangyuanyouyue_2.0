package com.fangyuanyouyue.wallet.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.wallet.model.PointGoods;

@Mapper
public interface PointGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PointGoods record);

    int insertSelective(PointGoods record);

    PointGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PointGoods record);

    int updateByPrimaryKeyWithBLOBs(PointGoods record);

    int updateByPrimaryKey(PointGoods record);

    List<PointGoods> selectList(@Param("start")Integer start,@Param("limit")Integer limit);
}