package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.InviteAward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InviteAwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InviteAward record);

    int insertSelective(InviteAward record);

    InviteAward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InviteAward record);

    int updateByPrimaryKey(InviteAward record);

    /**
     * 根据用户id获取用户奖励历史
     * @param userId
     * @return
     */
    List<InviteAward> selectAwardByUserId(@Param("userId") Integer userId);
}