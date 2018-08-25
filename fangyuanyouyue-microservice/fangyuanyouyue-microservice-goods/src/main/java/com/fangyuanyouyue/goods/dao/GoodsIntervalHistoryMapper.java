package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsIntervalHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsIntervalHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsIntervalHistory record);

    int insertSelective(GoodsIntervalHistory record);

    GoodsIntervalHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsIntervalHistory record);

    int updateByPrimaryKey(GoodsIntervalHistory record);

    /**
     * 根据抢购id获取降价历史
     * @param goodsId
     * @return
     */
    List<GoodsIntervalHistory> selectByGoodsId(@Param("goodsId") Integer goodsId,@Param("start")Integer start,@Param("limit")Integer limit);
}