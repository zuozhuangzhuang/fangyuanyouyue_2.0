package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.AppraisalImg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppraisalImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalImg record);

    int insertSelective(AppraisalImg record);

    AppraisalImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalImg record);

    int updateByPrimaryKey(AppraisalImg record);
}