package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsTopDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsTopDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsTopDetail record);

    int insertSelective(GoodsTopDetail record);

    GoodsTopDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsTopDetail record);

    int updateByPrimaryKey(GoodsTopDetail record);
}