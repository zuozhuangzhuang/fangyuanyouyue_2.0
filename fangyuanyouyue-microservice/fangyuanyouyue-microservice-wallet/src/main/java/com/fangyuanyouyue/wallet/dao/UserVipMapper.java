package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.UserVip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserVipMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVip record);

    int insertSelective(UserVip record);

    UserVip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVip record);

    int updateByPrimaryKey(UserVip record);

    /**
     * 根据userId获取用户会员信息
     * @param userId
     * @return
     */
    UserVip selectByUserId(@Param("userId")Integer userId);
}