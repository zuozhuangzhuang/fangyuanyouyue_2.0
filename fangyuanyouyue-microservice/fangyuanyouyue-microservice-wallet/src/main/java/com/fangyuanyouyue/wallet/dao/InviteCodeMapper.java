package com.fangyuanyouyue.wallet.dao;

import com.fangyuanyouyue.wallet.model.InviteCode;
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