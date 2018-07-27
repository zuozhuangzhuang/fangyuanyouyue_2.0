package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsBargain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsBargainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsBargain record);

    int insertSelective(GoodsBargain record);

    GoodsBargain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsBargain record);

    int updateByPrimaryKey(GoodsBargain record);

    /**
     * 根据用户ID和商品ID获取压价信息
     * @param userId
     * @param goodsId
     * @return
     */
    GoodsBargain selectByUserIdGoodsId(@Param("userId")Integer userId,@Param("goodsId")Integer goodsId);

    /**
     * 获取商品所有申请中的压价信息
     * @param goodsId
     * @return
     */
    List<GoodsBargain> selectAllByGoodsId(@Param("goodsId")Integer goodsId);
}