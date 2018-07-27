package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.GoodsComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsComment record);

    int insertSelective(GoodsComment record);

    GoodsComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsComment record);

    int updateByPrimaryKey(GoodsComment record);

    /**
     * 根据商品ID获取商品的评论列表（不包括回复）
     * @param goodsId
     * @return
     */
    List<Map<String,Object>> selectByGoodsId(@Param("goodsId") Integer goodsId,@Param("start") Integer start,@Param("limit")Integer limit);

    /**
     * 根据商品ID，评论（或回复）ID获取回复列表
     * @param commentId
     * @param goodsId
     * @param start
     * @param limit
     * @return
     */
    List<Map<String,Object>> selectMapByGoodsIdCommentId(@Param("commentId") Integer commentId, @Param("goodsId") Integer goodsId,@Param("start")Integer start,@Param("limit")Integer limit);

    Map<String,Object> selectByCommentId(@Param("commentId") Integer commentId);

    Integer selectCount(Integer goodsId);
}