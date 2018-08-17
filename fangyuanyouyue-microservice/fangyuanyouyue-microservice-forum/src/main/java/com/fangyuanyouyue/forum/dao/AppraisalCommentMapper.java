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

    int countByViewPoint(Integer appraisalId,Integer viewpoint);
    
    List<AppraisalComment> selectByAppraisalId(@Param("appraisalId")Integer appraisalId,@Param("start")Integer start,@Param("limit")Integer limit);

}