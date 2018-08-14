package com.fangyuanyouyue.order.dao;

import com.fangyuanyouyue.order.model.OrderComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderComment record);

    int insertSelective(OrderComment record);

    OrderComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderComment record);

    int updateByPrimaryKey(OrderComment record);

    /**
     * 根据订单id获取评论数据
     * @param orderId
     * @return
     */
    OrderComment selectByOrder(@Param("orderId")Integer orderId);
}