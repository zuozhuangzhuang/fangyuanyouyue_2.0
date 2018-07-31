package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsAppraisalDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsAppraisalDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsAppraisalDetail record);

    int insertSelective(GoodsAppraisalDetail record);

    GoodsAppraisalDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsAppraisalDetail record);

    int updateByPrimaryKeyWithBLOBs(GoodsAppraisalDetail record);

    int updateByPrimaryKey(GoodsAppraisalDetail record);

    GoodsAppraisalDetail selectByUserIdGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
}