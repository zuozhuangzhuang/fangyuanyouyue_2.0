package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsTopOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsTopOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsTopOrder record);

    int insertSelective(GoodsTopOrder record);

    GoodsTopOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsTopOrder record);

    int updateByPrimaryKey(GoodsTopOrder record);

    /**
     * 根据订单号获取置顶订单
     * @param orderNo
     * @return
     */
    GoodsTopOrder selectByOrderNo(@Param("orderNo")String orderNo);
}