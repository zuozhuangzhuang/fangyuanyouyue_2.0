package com.fangyuanyouyue.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.user.model.SysRole;

@Mapper
public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> selectAll();
    
    List<SysRole> selectByMenuId(Integer menuId);
}