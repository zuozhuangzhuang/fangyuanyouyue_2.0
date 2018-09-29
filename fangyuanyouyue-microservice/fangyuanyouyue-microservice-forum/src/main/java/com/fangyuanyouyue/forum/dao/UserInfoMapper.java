package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.forum.model.UserInfo;

@Mapper
public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}