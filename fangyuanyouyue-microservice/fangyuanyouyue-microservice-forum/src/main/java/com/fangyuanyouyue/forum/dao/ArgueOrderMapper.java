package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.ArgueOrder;

@Mapper
public interface ArgueOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArgueOrder record);

    int insertSelective(ArgueOrder record);

    ArgueOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArgueOrder record);

    int updateByPrimaryKey(ArgueOrder record);

    /**
     * 根据订单号获取全民鉴定订单信息
     * @param orderNo
     * @return
     */
    ArgueOrder selectByOrderNo(@Param("orderNo")String orderNo);
}