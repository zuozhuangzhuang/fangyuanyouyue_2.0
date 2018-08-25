package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.AppraisalComment;

@Mapper
public interface AppraisalCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalComment record);

    int insertSelective(AppraisalComment record);

    AppraisalComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalComment record);

    int updateByPrimaryKey(AppraisalComment record);
    
    int countById(Integer appraisalId);

    int countByViewPoint(@Param("appraisalId") Integer appraisalId,@Param("viewpoint") Integer viewpoint);

    /**
     * 根据鉴定id获取评论列表
     * @param appraisalId
     * @param start
     * @param limit
     * @return
     */
    List<AppraisalComment> selectByAppraisalId(@Param("appraisalId")Integer appraisalId,@Param("start")Integer start,@Param("limit")Integer limit);

    /**
     * 根据用户id和鉴定id获取评论
     * @param userId
     * @param appraisalId
     * @return
     */
    AppraisalComment selectByAppraisalIdUserId(@Param("userId")Integer userId,@Param("appraisalId")Integer appraisalId);

    /**
     * 根据鉴定id获取评论中点赞数最多的评论
     * @param appraisalId
     * @return
     */
    Integer selectMostLikesCommentIdByAppraisalId(@Param("appraisalId")Integer appraisalId);
}