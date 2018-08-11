package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.OrderRefund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderRefundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefund record);

    int insertSelective(OrderRefund record);

    OrderRefund selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRefund record);

    int updateByPrimaryKey(OrderRefund record);

    /**
     * 根据订单ID和状态获取退货信息
     * @param orderId
     * @param status
     * @return
     */
    OrderRefund selectByOrderIdStatus(@Param("orderId")Integer orderId,@Param("status")Integer status);
}