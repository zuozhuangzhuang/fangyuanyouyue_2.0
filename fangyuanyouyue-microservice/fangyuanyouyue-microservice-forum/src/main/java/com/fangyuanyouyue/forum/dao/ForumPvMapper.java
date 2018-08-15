package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.forum.model.ForumPv;

@Mapper
public interface ForumPvMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumPv record);

    int insertSelective(ForumPv record);

    ForumPv selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumPv record);

    int updateByPrimaryKey(ForumPv record);
}