package com.fangyuanyouyue.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.user.model.SysRoleMenu;

@Mapper
public interface SysRoleMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleMenu record);

    int insertSelective(SysRoleMenu record);

    SysRoleMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleMenu record);

    int updateByPrimaryKey(SysRoleMenu record);

    List<SysRoleMenu> selectAll();

    List<SysRoleMenu> selectByRoleId(Integer roleId);

    List<SysRoleMenu> selectByMenuId(Integer menuId);

    int deleteByRoleId(Integer roleId);

    int deleteByMenuId(Integer menuId);
}