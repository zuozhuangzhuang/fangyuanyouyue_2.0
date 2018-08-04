package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.CommentLikes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommentLikes record);

    int insertSelective(CommentLikes record);

    CommentLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommentLikes record);

    int updateByPrimaryKey(CommentLikes record);

    /**
     * 根据用户、评论ID获取点赞信息
     * @param userId
     * @param commentId
     * @return
     */
    CommentLikes selectByUserId(@Param("userId")Integer userId,@Param("commentId")Integer commentId);
}