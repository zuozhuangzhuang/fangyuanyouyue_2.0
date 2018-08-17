package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.ForumColumn;
@Mapper
public interface ForumColumnMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(ForumColumn record);

    int insertSelective(ForumColumn record);

    ForumColumn selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumColumn record);

    int updateByPrimaryKey(ForumColumn record);
    
    List<ForumColumn> selectPage(@Param("start")Integer start,@Param("limit")Integer limit);

    List<ForumColumn> selectChosen(@Param("isChosen")Integer isChosen);

    /**
     * 根据用户id获取专栏，每个用户只能有一个专栏
     * @param userId
     * @return
     */
    ForumColumn selectByUserId(@Param("userId")Integer userId);
}