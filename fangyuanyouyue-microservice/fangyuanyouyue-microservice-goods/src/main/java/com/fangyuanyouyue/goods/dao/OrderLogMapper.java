package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.OrderLog;

public interface OrderLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderLog record);

    int insertSelective(OrderLog record);

    OrderLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderLog record);

    int updateByPrimaryKey(OrderLog record);
}