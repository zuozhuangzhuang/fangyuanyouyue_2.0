package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.ForumCommentLikes;

public interface ForumCommentLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumCommentLikes record);

    int insertSelective(ForumCommentLikes record);

    ForumCommentLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumCommentLikes record);

    int updateByPrimaryKey(ForumCommentLikes record);
}