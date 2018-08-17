package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.forum.model.ForumPv;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ForumPvMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumPv record);

    int insertSelective(ForumPv record);

    ForumPv selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumPv record);

    int updateByPrimaryKey(ForumPv record);

    int countById(Integer forumId);

    int countByUserId(Integer forumId,Integer userId);

    /**
     * 根据用户id和帖子id获取浏览信息
     * @param userId
     * @param forumId
     * @return
     */
    ForumPv selectByUserIdColumnId(@Param("userId")Integer userId,@Param("forumId")Integer forumId);
}