package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsQuickSearch;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsQuickSearchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsQuickSearch record);

    int insertSelective(GoodsQuickSearch record);

    GoodsQuickSearch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsQuickSearch record);

    int updateByPrimaryKey(GoodsQuickSearch record);

    List<GoodsQuickSearch> getQuickSearchList();
}