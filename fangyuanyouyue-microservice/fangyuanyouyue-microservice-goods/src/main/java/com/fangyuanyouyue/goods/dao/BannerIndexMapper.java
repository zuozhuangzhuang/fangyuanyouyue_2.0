package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.BannerIndex;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannerIndexMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BannerIndex record);

    int insertSelective(BannerIndex record);

    BannerIndex selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BannerIndex record);

    int updateByPrimaryKey(BannerIndex record);

    /**
     * 获取首页轮播图列表
     * @return
     */
    List<BannerIndex> getBanner(Integer type);
}