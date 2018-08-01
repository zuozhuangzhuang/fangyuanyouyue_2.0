package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.OrderPay;

public interface OrderPayMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderPay record);

    int insertSelective(OrderPay record);

    OrderPay selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderPay record);

    int updateByPrimaryKey(OrderPay record);
}