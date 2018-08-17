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
     * 根据用户id获取评论，每个用户只能发布一次评论
     * @param userId
     * @return
     */
    AppraisalComment selectByUserId(@Param("userId")Integer userId);
}