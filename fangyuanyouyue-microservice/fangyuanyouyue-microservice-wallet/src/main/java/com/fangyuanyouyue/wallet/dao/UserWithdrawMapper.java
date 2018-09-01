package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserWithdraw;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserWithdrawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserWithdraw record);

    int insertSelective(UserWithdraw record);

    UserWithdraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserWithdraw record);

    int updateByPrimaryKey(UserWithdraw record);
}