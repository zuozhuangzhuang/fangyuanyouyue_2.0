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
     * 获取用户收藏的专栏列表
     * @param userId
     * @param start
     * @param limit
     * @param collectType
     * @return
     */
    List<ForumColumn> selectCollectList(@Param("userId")Integer userId,@Param("start")Integer start,@Param("limit")Integer limit,@Param("collectType")Integer collectType);
}