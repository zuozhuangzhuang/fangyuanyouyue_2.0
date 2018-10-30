package com.fangyuanyouyue.forum.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.ForumComment;
@Mapper
public interface ForumCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumComment record);

    int insertSelective(ForumComment record);

    ForumComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumComment record);

    int updateByPrimaryKey(ForumComment record);

    int countAll();
    
    int countById(Integer forumId);
    
    int countByCommentId(Integer commentId);
    
    List<ForumComment> selectByForumId(@Param("forumId")Integer forumId,@Param("start")Integer start,@Param("limit")Integer limit);

    List<ForumComment> selectByCommentId(@Param("commentId")Integer commentId,@Param("start")Integer start,@Param("limit")Integer limit);

    /**
     * 获取我的评论列表
     * @param userId
     * @param start
     * @param limit
     * @return
     */
    List<Map> selectByUserId(@Param("userId")Integer userId, @Param("start")Integer start, @Param("limit")Integer limit, @Param("type")Integer type);

    /**
     * 根据评论id获取所有回复
     * @param commentId
     * @return
     */
    List<ForumComment> selectReplyByCommentId(@Param("commentId")Integer commentId);
}