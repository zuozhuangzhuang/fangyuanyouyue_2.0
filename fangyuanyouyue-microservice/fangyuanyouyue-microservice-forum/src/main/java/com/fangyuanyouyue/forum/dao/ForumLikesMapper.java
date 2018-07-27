package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.ForumLikes;

public interface ForumLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumLikes record);

    int insertSelective(ForumLikes record);

    ForumLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumLikes record);

    int updateByPrimaryKey(ForumLikes record);

    int countById(Integer forumId);

    List<ForumLikes> selectByForumId(@Param("forumId")Integer forumId,@Param("start")Integer start,@Param("limit")Integer limit);
}