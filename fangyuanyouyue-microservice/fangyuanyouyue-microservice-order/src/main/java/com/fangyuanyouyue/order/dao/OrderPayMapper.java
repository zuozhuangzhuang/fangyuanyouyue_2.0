package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.OrderPay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderPayMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderPay record);

    int insertSelective(OrderPay record);

    OrderPay selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderPay record);

    int updateByPrimaryKey(OrderPay record);

    OrderPay selectByOrderId(@Param("orderId")Integer orderId);

    /**
     * 根据状态获取订单支付表列表
     * @param status
     * @return
     */
    List<OrderPay> selectByStatus(@Param("status")Integer status, @Param("sendTime")Date sendTime);
}