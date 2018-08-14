package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.AppraisalDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppraisalDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalDetail record);

    int insertSelective(AppraisalDetail record);

    AppraisalDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalDetail record);

    int updateByPrimaryKeyWithBLOBs(AppraisalDetail record);

    int updateByPrimaryKey(AppraisalDetail record);
}