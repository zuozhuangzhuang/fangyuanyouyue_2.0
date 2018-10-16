package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.ConfinedUser;
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
     * 根据代理码获取代理信息
     * @param code
     * @return
     */
    ConfinedUser selectByCode(@Param("code")String code);

    /**
     * 根据用户id获取代理信息
     * @param userId
     * @return
     */
    ConfinedUser selectByUserId(@Param("userId")Integer userId);
}