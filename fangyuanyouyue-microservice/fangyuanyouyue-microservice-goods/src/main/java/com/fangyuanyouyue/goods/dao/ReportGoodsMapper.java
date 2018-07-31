package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.ReportGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReportGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ReportGoods record);

    int insertSelective(ReportGoods record);

    ReportGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReportGoods record);

    int updateByPrimaryKey(ReportGoods record);

    /**
     * 根据用户ID和商品ID获取举报信息
     * @param userId
     * @param goodsId
     * @return
     */
    ReportGoods selectByUserIdGoodsId(@Param("userId") Integer userId, @Param("goodsId")Integer goodsId);
}