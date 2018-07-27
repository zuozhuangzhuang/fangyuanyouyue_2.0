package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsCorrelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsCorrelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCorrelation record);

    int insertSelective(GoodsCorrelation record);

    GoodsCorrelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCorrelation record);

    int updateByPrimaryKey(GoodsCorrelation record);

    List<GoodsCorrelation> getCorrelationsByGoodsId(Integer goodsId);

    List<Integer> selectCategoryIdByGoodsId(Integer goodsId);
}