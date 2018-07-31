package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserFans;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserFansMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFans record);

    int insertSelective(UserFans record);

    UserFans selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFans record);

    int updateByPrimaryKey(UserFans record);

    UserFans selectByUserIdToUserId(@Param("userId")Integer userId,@Param("toUserId")Integer toUserId);

    Integer fansCount(Integer userId);

    Integer collectCount(Integer userId);
}