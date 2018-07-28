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

    List<OrderInfo> getListByUserIdTypeStatus(@Param("userId")Integer userId,@Param("start") Integer start,@Param("limit")Integer limit,@Param("type")Integer type,@Param("status")Integer status);
}