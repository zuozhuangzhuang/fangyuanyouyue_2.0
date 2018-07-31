package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsImg record);

    int insertSelective(GoodsImg record);

    GoodsImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsImg record);

    int updateByPrimaryKey(GoodsImg record);

    /**
     * 根据商品ID获取图片列表
     * @param goodsId
     * @return
     */
    List<GoodsImg> getImgsByGoodsId(Integer goodsId);

    /**
     * 根据商品ID删除图片信息
     * @param goodsId
     */
    void deleteByGoodsId(@Param("goodsId")Integer goodsId);
}