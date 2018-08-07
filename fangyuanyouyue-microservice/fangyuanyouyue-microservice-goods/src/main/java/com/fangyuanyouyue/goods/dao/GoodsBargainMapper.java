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
     * 根据买家ID和商品ID获取压价信息
     * @param userId
     * @param goodsId
     * @return
     */
    List<GoodsBargain> selectByUserIdGoodsId(@Param("userId")Integer userId, @Param("goodsId")Integer goodsId,@Param("status")Integer status);

    /**
     * 获取商品所有申请中的压价信息
     * @param goodsId
     * @return
     */
    List<GoodsBargain> selectAllByGoodsId(@Param("goodsId")Integer goodsId,@Param("status")Integer status);

    /**
     * 根据用户ID获取所有压价列表，不区分状态
     * @param userId
     * @return
     */
    List<GoodsBargain> selectAllByUserId(@Param("userId")Integer userId);

    /**
     * 根据用户ID获取所有压过价的商品ID列表
     * @param userId
     * @return
     */
    List<Integer> selectGoodsIdsByUserId(@Param("userId")Integer userId,@Param("start")Integer start,@Param("limit")Integer limit);
}