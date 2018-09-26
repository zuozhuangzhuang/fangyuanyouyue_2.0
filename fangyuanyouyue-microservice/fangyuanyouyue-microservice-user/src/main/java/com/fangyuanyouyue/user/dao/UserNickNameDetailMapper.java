package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserNickNameDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserNickNameDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserNickNameDetail record);

    int insertSelective(UserNickNameDetail record);

    UserNickNameDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserNickNameDetail record);

    int updateByPrimaryKey(UserNickNameDetail record);
}