package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.ForumInfo;

public interface ForumInfoMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(ForumInfo record);

    int insertSelective(ForumInfo record);

    ForumInfo selectByPrimaryKey(Integer id);

    ForumInfo selectDetailByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumInfo record);

    int updateByPrimaryKeyWithBLOBs(ForumInfo record);

    int updateByPrimaryKey(ForumInfo record);
    
    List<ForumInfo> selectList(@Param("columnId")Integer columnId,@Param("start")Integer start,@Param("limit")Integer limit);
}