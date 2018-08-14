package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.AppraisalLikes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppraisalLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalLikes record);

    int insertSelective(AppraisalLikes record);

    AppraisalLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalLikes record);

    int updateByPrimaryKey(AppraisalLikes record);
}