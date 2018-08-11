package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserBalanceDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBalanceDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBalanceDetail record);

    int insertSelective(UserBalanceDetail record);

    UserBalanceDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBalanceDetail record);

    int updateByPrimaryKey(UserBalanceDetail record);
}