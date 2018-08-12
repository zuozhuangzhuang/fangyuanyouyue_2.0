package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.ForumColumnType;

public interface ForumColumnTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumColumnType record);

    int insertSelective(ForumColumnType record);

    ForumColumnType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumColumnType record);

    int updateByPrimaryKey(ForumColumnType record);
}