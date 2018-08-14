package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.ForumColumnType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumColumnTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumColumnType record);

    int insertSelective(ForumColumnType record);

    ForumColumnType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumColumnType record);

    int updateByPrimaryKey(ForumColumnType record);
}