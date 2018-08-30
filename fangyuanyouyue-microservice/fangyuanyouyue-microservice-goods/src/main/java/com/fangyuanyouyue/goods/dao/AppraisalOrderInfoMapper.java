package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.AppraisalOrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppraisalOrderInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalOrderInfo record);

    int insertSelective(AppraisalOrderInfo record);

    AppraisalOrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalOrderInfo record);

    int updateByPrimaryKey(AppraisalOrderInfo record);

    /**
     * 根据订单号获取官方鉴定订单
     * @param orderNo
     * @return
     */
    AppraisalOrderInfo selectByOrderNo(@Param("orderNo")String orderNo);
}