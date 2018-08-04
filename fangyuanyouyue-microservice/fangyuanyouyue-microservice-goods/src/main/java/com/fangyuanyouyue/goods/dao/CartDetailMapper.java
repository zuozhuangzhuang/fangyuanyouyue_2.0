package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.CartDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CartDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CartDetail record);

    int insertSelective(CartDetail record);

    CartDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CartDetail record);

    int updateByPrimaryKey(CartDetail record);


    List<CartDetail> selectByCartId(Integer cartId);

    /**
     * 根据购物车ID商品ID获取订单详情表信息
     * @param cartId
     * @param goodsId
     * @return
     */
    CartDetail selectByCartIdGoodsId(@Param("cartId") Integer cartId, @Param("goodsId")Integer goodsId);

    /**
     *
     * @param cartId
     * @param userId
     * @return
     */
    List<Map<String,Object>> selectByCartIdUserId(@Param("cartId")Integer cartId, @Param("userId") Integer userId);

}