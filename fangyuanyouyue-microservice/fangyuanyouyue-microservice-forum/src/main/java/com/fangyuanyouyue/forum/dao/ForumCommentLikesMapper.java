package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.forum.model.ForumCommentLikes;

@Mapper
public interface ForumCommentLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumCommentLikes record);

    int insertSelective(ForumCommentLikes record);

    ForumCommentLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumCommentLikes record);

    int updateByPrimaryKey(ForumCommentLikes record);

    int countById(Integer commentId);
    
}