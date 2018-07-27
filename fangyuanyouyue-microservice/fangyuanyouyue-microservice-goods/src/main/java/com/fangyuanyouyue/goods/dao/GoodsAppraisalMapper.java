package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsAppraisal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsAppraisalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsAppraisal record);

    int insertSelective(GoodsAppraisal record);

    GoodsAppraisal selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsAppraisal record);

    int updateByPrimaryKey(GoodsAppraisal record);
}