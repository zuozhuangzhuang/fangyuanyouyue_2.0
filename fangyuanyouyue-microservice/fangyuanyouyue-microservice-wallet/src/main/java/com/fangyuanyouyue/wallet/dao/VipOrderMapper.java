package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.VipOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VipOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VipOrder record);

    int insertSelective(VipOrder record);

    VipOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VipOrder record);

    int updateByPrimaryKey(VipOrder record);

    /**
     * 根据订单号获取会员订单表
     * @param orderNo
     * @return
     */
    VipOrder selectByOrderNo(@Param("orderNo")String orderNo);
}