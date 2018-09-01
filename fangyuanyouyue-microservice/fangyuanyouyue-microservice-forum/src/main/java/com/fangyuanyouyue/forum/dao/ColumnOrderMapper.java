package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.ColumnOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ColumnOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ColumnOrder record);

    int insertSelective(ColumnOrder record);

    ColumnOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ColumnOrder record);

    int updateByPrimaryKey(ColumnOrder record);

    /**
     * 根据订单号获取支付中的专栏申请订单
     * @param orderNo
     * @return
     */
    ColumnOrder selectByOrderNo(@Param("orderNo")String orderNo);
}