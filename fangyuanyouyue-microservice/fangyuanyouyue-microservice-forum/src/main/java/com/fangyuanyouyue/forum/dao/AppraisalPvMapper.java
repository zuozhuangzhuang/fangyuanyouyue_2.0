package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.forum.model.AppraisalPv;

@Mapper
public interface AppraisalPvMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalPv record);

    int insertSelective(AppraisalPv record);

    AppraisalPv selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalPv record);

    int updateByPrimaryKey(AppraisalPv record);
}