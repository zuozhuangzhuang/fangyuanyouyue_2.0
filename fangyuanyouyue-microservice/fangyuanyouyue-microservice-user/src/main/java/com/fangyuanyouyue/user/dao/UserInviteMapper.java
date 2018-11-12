package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserInvite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInviteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInvite record);

    int insertSelective(UserInvite record);

    UserInvite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInvite record);

    int updateByPrimaryKey(UserInvite record);
}