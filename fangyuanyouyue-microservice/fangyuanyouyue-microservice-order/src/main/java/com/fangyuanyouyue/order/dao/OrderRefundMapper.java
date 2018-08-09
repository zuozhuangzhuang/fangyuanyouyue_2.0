package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.OrderRefund;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderRefundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRefund record);

    int insertSelective(OrderRefund record);

    OrderRefund selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRefund record);

    int updateByPrimaryKey(OrderRefund record);
}