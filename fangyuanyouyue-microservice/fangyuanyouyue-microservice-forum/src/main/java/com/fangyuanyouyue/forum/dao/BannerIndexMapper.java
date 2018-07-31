package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.BannerIndex;

public interface BannerIndexMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BannerIndex record);

    int insertSelective(BannerIndex record);

    BannerIndex selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BannerIndex record);

    int updateByPrimaryKey(BannerIndex record);

    List<BannerIndex> selectAll(@Param("status")Integer status);
    
}