package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.ForumColumnApply;

public interface ForumColumnApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumColumnApply record);

    int insertSelective(ForumColumnApply record);

    ForumColumnApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumColumnApply record);

    int updateByPrimaryKey(ForumColumnApply record);
}