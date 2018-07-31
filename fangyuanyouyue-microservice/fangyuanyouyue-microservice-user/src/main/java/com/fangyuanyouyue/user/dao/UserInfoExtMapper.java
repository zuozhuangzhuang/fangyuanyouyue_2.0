package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserInfoExt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoExtMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfoExt record);

    int insertSelective(UserInfoExt record);

    UserInfoExt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfoExt record);

    int updateByPrimaryKey(UserInfoExt record);

    /**
     * 根据用户ID获取用户扩展信息
     * @param userId
     * @return
     */
    UserInfoExt selectByUserId(int userId);
}