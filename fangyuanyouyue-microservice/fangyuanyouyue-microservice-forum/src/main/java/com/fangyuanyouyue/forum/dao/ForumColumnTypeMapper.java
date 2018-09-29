package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.forum.model.ForumColumnType;

@Mapper
public interface ForumColumnTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumColumnType record);

    int insertSelective(ForumColumnType record);

    ForumColumnType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumColumnType record);

    int updateByPrimaryKey(ForumColumnType record);

    /**
     * 获取所有分类
     * @return
     */
    List<ForumColumnType> getAll();
}