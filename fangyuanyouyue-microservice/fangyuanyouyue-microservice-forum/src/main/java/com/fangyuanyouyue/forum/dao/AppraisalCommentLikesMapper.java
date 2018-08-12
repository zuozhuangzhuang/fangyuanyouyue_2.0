package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.AppraisalCommentLikes;

public interface AppraisalCommentLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalCommentLikes record);

    int insertSelective(AppraisalCommentLikes record);

    AppraisalCommentLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalCommentLikes record);

    int updateByPrimaryKey(AppraisalCommentLikes record);
}