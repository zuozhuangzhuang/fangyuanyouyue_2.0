package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.OrderInfo;
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
     * 获取未拆单订单列表
     * @param userId
     * @param start
     * @param limit
     * @param status
     * @return
     */
    List<OrderInfo> getListByUserIdStatus(@Param("userId")Integer userId, @Param("start") Integer start, @Param("limit")Integer limit, @Param("status")Integer status);

    /**
     * 根据卖家获取已拆单订单列表
     * @param sellerId
     * @param start
     * @param limit
     * @param status
     * @return
     */
    List<OrderInfo> getOrderBySellerId(@Param("sellerId")Integer sellerId, @Param("start") Integer start, @Param("limit")Integer limit, @Param("status")Integer status);

    /**
     * 获取总订单的子订单
     * @param userId
     * @return
     */
    List<OrderInfo> selectChildOrderByOrderId(@Param("userId")Integer userId,@Param("mainOrderId")Integer mainOrderId);

    /**
     * 根据商品ID获取进行中的订单
     * @param goodsId
     * @return
     */
    OrderInfo selectByGoodsId(@Param("goodsId")Integer goodsId);

    /**
     * 根据用户ID、订单ID获取订单信息
     * @param orderId
     * @param userId
     * @return
     */
    OrderInfo getOrderByUserIdOrderId(@Param("orderId")Integer orderId,@Param("userId")Integer userId);
}