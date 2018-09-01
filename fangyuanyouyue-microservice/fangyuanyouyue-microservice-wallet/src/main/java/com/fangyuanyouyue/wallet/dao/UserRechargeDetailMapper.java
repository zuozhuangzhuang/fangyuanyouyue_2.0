package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserRechargeDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRechargeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRechargeDetail record);

    int insertSelective(UserRechargeDetail record);

    UserRechargeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRechargeDetail record);

    int updateByPrimaryKey(UserRechargeDetail record);

    /**
     * 获取充值订单
     * @param orderNo
     * @return
     */
    UserRechargeDetail selectByOrderNo(@Param("orderNo")String orderNo);
}