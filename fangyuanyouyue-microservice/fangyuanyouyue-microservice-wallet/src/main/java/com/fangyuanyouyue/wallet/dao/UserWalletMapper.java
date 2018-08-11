package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserWallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserWalletMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserWallet record);

    int insertSelective(UserWallet record);

    UserWallet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserWallet record);

    int updateByPrimaryKey(UserWallet record);

    /**
     * 根据用户ID获取钱包
     * @param userId
     * @return
     */
    UserWallet selectByUserId(@Param("userId")Integer userId);
}