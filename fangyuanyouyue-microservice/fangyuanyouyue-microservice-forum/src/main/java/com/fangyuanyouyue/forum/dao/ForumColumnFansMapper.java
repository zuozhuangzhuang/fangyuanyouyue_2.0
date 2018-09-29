package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.forum.model.ForumColumnFans;

@Mapper
public interface ForumColumnFansMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumColumnFans record);

    int insertSelective(ForumColumnFans record);

    ForumColumnFans selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumColumnFans record);

    int updateByPrimaryKey(ForumColumnFans record);
}