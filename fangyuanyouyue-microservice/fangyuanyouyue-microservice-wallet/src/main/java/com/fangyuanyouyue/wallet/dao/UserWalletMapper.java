package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserWallet;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserWalletMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserWallet record);

    int insertSelective(UserWallet record);

    UserWallet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserWallet record);

    int updateByPrimaryKey(UserWallet record);
}