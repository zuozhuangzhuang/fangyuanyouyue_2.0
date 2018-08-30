package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsCorrelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsCorrelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCorrelation record);

    int insertSelective(GoodsCorrelation record);

    GoodsCorrelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCorrelation record);

    int updateByPrimaryKey(GoodsCorrelation record);

    /**
     * 根据商品id获取分类关联表列表
     * @param goodsId
     * @return
     */
    List<GoodsCorrelation> getCorrelationsByGoodsId(Integer goodsId);

    /**
     * 根据商品id获取分类id列表
     * @param goodsId
     * @return
     */
    List<Integer> selectCategoryIdByGoodsId(Integer goodsId);

    /**
     * 根据分类id、商品id获取分类关联信息
     * @param category
     * @param goodsId
     * @return
     */
    GoodsCorrelation selectCorrekationsByGoodsIdCategoryId(@Param("categoryId")Integer category,@Param("goodsId")Integer goodsId);
}