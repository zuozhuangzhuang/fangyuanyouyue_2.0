package com.fangyuanyouyue.forum.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fangyuanyouyue.forum.model.ForumPv;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据专栏id获取访问量
     * @param columnId
     * @return
     */
    Integer getCountByColumnId(@Param("columnId")Integer columnId,@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("type")Integer type);

    /**
     * 获取每日返利专栏
     * @return
     */
    List<Map<String,Object>> dailyWage();
}