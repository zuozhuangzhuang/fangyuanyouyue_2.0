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
}