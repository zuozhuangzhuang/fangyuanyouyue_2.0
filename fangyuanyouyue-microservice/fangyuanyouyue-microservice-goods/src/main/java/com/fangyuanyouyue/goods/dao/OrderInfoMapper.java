package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    /**
     * 根据用户id、商品id、订单状态获取订单
     * @return
     * @param userId
     * @param goodsId
     */
    List<OrderInfo> selectByUserIdGoodsIdStauts(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
}