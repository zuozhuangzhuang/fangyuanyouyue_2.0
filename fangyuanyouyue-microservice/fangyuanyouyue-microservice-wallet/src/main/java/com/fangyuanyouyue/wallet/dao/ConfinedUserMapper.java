package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.ConfinedUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ConfinedUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfinedUser record);

    int insertSelective(ConfinedUser record);

    ConfinedUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfinedUser record);

    int updateByPrimaryKey(ConfinedUser record);

    /**
     * 根据userId和status获取限制用户
     * @param userId
     * @param status
     * @return
     */
    ConfinedUser selectByUserIdStatus(@Param("userId")Integer userId,@Param("status")Integer status);
}