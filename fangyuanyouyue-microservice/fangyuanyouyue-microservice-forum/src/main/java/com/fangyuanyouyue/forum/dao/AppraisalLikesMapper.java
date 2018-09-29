package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.AppraisalLikes;

@Mapper
public interface AppraisalLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalLikes record);

    int insertSelective(AppraisalLikes record);

    AppraisalLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalLikes record);

    int updateByPrimaryKey(AppraisalLikes record);
    
    int countById(Integer appraisalId);
    
    int countByUserId(Integer appraisalId,Integer userId);

    /**
     * 根据用户id和鉴定id获取点赞信息
     * @param appraisalId
     * @param userId
     * @return
     */
    AppraisalLikes selectByAppraisalIdUserId(@Param("appraisalId")Integer appraisalId, @Param("userId")Integer userId);
}