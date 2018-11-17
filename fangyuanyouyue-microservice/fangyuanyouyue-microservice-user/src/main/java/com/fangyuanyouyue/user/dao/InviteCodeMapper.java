package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.InviteCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InviteCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InviteCode record);

    int insertSelective(InviteCode record);

    InviteCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InviteCode record);

    int updateByPrimaryKey(InviteCode record);

    /**
     * 根据邀请码获取用户信息
     * @param inviteCode
     * @return
     */
    InviteCode getUserByCode(@Param("inviteCode")String inviteCode);

    /**
     * 根据用户id获取用户邀请码
     * @param userId
     * @return
     */
    InviteCode selectUserCodeByUserId(@Param("userId")Integer userId);
}