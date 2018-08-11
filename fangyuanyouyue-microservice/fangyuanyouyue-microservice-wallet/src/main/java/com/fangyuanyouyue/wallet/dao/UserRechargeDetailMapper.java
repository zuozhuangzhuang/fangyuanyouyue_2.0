package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserRechargeDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRechargeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRechargeDetail record);

    int insertSelective(UserRechargeDetail record);

    UserRechargeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRechargeDetail record);

    int updateByPrimaryKey(UserRechargeDetail record);
}