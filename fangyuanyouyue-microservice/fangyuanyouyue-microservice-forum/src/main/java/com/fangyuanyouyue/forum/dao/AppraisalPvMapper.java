package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.AppraisalPv;

@Mapper
public interface AppraisalPvMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalPv record);

    int insertSelective(AppraisalPv record);

    AppraisalPv selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalPv record);

    int updateByPrimaryKey(AppraisalPv record);

    int countById(Integer appraisalId);
    
    int countByUserId(Integer appraisalId,Integer userId);

    /**
     * 根据用户id和鉴定id获取浏览信息
     * @param userId
     * @param appraisalId
     * @return
     */
    AppraisalPv selectByUserIdAppraisalId(@Param("userId")Integer userId,@Param("appraisalId")Integer appraisalId);
}