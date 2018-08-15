package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.ForumPv;

public interface ForumPvMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumPv record);

    int insertSelective(ForumPv record);

    ForumPv selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumPv record);

    int updateByPrimaryKey(ForumPv record);
}