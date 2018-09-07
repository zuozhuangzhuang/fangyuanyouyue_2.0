package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDetail record);

    int insertSelective(OrderDetail record);

    OrderDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDetail record);

    int updateByPrimaryKeyWithBLOBs(OrderDetail record);

    int updateByPrimaryKey(OrderDetail record);

    /**
     * 根据订单ID获取订单详情列表
     * @param orderId
     * @return
     */
    List<OrderDetail> selectByOrderId(@Param("orderId") Integer orderId);


    /**
     * 卖家根据商品ID获取进行中的订单
     * @param userId
     * @param goodsId
     * @return
     */
    OrderDetail selectOrderByGoodsIdStatus(@Param("userId")Integer userId, @Param("goodsId")Integer goodsId,@Param("type")Integer type);

    /**
     * 根据买家id和商品id获取订单详情
     * @param userId
     * @param goodsId
     * @return
     */
    OrderDetail getByUserIdGoodsId(@Param("userId")Integer userId, @Param("goodsId")Integer goodsId);
}