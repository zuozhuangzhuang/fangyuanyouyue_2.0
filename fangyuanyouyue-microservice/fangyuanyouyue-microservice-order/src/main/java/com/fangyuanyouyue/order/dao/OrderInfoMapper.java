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
     * 获取订单列表
     * @param userId
     * @param start
     * @param limit
     * @param status
     * @return
     */
    List<OrderInfo> getListByUserIdStatus(@Param("userId")Integer userId, @Param("start") Integer start, @Param("limit")Integer limit, @Param("status")Integer status);

    /**
     * 根据卖家获取订单列表
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
}