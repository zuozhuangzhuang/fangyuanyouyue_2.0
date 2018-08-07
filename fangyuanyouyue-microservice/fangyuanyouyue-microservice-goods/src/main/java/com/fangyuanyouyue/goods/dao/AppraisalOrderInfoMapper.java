package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.AppraisalOrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppraisalOrderInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalOrderInfo record);

    int insertSelective(AppraisalOrderInfo record);

    AppraisalOrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalOrderInfo record);

    int updateByPrimaryKey(AppraisalOrderInfo record);
}