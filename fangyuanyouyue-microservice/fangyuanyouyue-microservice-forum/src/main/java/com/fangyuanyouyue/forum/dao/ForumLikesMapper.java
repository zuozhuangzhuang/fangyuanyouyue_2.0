package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.ForumLikes;
@Mapper
public interface ForumLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumLikes record);

    int insertSelective(ForumLikes record);

    ForumLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumLikes record);

    int updateByPrimaryKey(ForumLikes record);

    int countById(Integer forumId);

    int countByUserId(Integer forumId,Integer userId);
    

    List<ForumLikes> selectByForumId(@Param("forumId")Integer forumId,@Param("start")Integer start,@Param("limit")Integer limit);
}