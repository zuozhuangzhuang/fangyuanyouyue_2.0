package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.AppraisalComment;

public interface AppraisalCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalComment record);

    int insertSelective(AppraisalComment record);

    AppraisalComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalComment record);

    int updateByPrimaryKey(AppraisalComment record);
    
    int countById(Integer appraisalId);
}