package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.InviteCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InviteCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InviteCode record);

    int insertSelective(InviteCode record);

    InviteCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InviteCode record);

    int updateByPrimaryKey(InviteCode record);
}