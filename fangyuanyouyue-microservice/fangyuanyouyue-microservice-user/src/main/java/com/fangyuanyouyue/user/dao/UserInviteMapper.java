package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserInvite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInviteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInvite record);

    int insertSelective(UserInvite record);

    UserInvite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInvite record);

    int updateByPrimaryKey(UserInvite record);

    /**
     * 根据用户id获取用户邀请信息
     * @param userId
     * @return
     */
    List<UserInvite> selectUserInviteById(@Param("userId")Integer userId);
}