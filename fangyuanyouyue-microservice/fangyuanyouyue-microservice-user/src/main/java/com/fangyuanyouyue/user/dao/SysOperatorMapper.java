package com.fangyuanyouyue.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.user.model.SysOperator;

@Mapper
public interface SysOperatorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysOperator record);

    int insertSelective(SysOperator record);

    SysOperator selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysOperator record);

    int updateByPrimaryKey(SysOperator record);
    
    SysOperator selectLogin(String userCode,String password);
    
    List<SysOperator> selectAll();

    List<SysOperator> selectByRoleId(Integer roleId);
    
}