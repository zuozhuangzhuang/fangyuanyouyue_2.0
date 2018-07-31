package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.UserVip;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserVipMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVip record);

    int insertSelective(UserVip record);

    UserVip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVip record);

    int updateByPrimaryKey(UserVip record);

    /**
     * 根据用户ID获取用户会员信息
     * @param userId
     * @return
     */
    UserVip getUserVipByUserId(Integer userId);
}