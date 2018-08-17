package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.AppraisalCommentLikes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppraisalCommentLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalCommentLikes record);

    int insertSelective(AppraisalCommentLikes record);

    AppraisalCommentLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalCommentLikes record);

    int updateByPrimaryKey(AppraisalCommentLikes record);
    
    int countById(Integer commentId);
    
    int countByUserId(Integer commentId,Integer userId);

    /**
     * 根据用户id和评论id获取点赞信息
     * @param commentId
     * @param userId
     * @return
     */
    AppraisalCommentLikes selectByCommentIdUserId(@Param("commentId")Integer commentId, @Param("userId")Integer userId);
}