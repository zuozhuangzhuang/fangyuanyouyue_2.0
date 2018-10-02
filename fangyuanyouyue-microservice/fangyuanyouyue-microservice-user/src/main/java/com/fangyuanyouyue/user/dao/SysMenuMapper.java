package com.fangyuanyouyue.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.user.model.SysMenu;

@Mapper
public interface SysMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<SysMenu> selectAll();
    
    List<SysMenu> selectChildren(Integer parentId);
    
    List<SysMenu> selectByRoleId(Integer roleId);
    
    int deleteByParentId(Integer parentId);
}