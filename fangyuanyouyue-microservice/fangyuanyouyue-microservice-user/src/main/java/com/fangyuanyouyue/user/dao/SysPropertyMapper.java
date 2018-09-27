package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.SysProperty;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysPropertyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysProperty record);

    int insertSelective(SysProperty record);

    SysProperty selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysProperty record);

    int updateByPrimaryKeyWithBLOBs(SysProperty record);

    int updateByPrimaryKey(SysProperty record);
}