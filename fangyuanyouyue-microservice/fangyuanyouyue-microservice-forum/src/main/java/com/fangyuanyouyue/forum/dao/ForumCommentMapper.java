package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.ForumComment;

public interface ForumCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumComment record);

    int insertSelective(ForumComment record);

    ForumComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumComment record);

    int updateByPrimaryKey(ForumComment record);

    int countAll();
    
    int countById(Integer forumId);
    
    List<ForumComment> selectByForumId(@Param("forumId")Integer forumId,@Param("start")Integer start,@Param("limit")Integer limit);
    
}