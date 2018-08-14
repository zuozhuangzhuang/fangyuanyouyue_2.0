package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.OrderRefund;
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
     * 获取退货详情
     * @param orderId
     * @return
     */
    OrderRefund selectByUserIdOrderId(@Param("orderId")Integer orderId);

}