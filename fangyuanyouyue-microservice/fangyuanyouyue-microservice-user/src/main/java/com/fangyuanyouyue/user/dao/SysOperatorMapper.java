package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.SysOperator;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysOperatorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysOperator record);

    int insertSelective(SysOperator record);

    SysOperator selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysOperator record);

    int updateByPrimaryKey(SysOperator record);
}