package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.ConfinedUser;

public interface ConfinedUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfinedUser record);

    int insertSelective(ConfinedUser record);

    ConfinedUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfinedUser record);

    int updateByPrimaryKey(ConfinedUser record);
}