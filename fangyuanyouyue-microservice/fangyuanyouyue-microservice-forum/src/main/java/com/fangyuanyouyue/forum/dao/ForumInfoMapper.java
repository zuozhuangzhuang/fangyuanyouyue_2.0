package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.ForumInfo;
@Mapper
public interface ForumInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ForumInfo record);

    int insertSelective(ForumInfo record);

    ForumInfo selectByPrimaryKey(Integer id);

    ForumInfo selectDetailByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumInfo record);

    int updateByPrimaryKeyWithBLOBs(ForumInfo record);

    int updateByPrimaryKey(ForumInfo record);

    List<ForumInfo> selectList(@Param("columnId")Integer columnId,@Param("userId")Integer userId,@Param("type")Integer type,
                               @Param("keyword")String keyword,@Param("start")Integer start,@Param("limit")Integer limit,@Param("searchType")Integer searchType);

    /**
     * 获取用户收藏的视频列表
     * @param userId
     * @param start
     * @param limit
     * @param collectType
     * @param search
     * @return
     */
    List<ForumInfo> selectCollectList(@Param("userId")Integer userId,@Param("start")Integer start,@Param("limit")Integer limit,@Param("collectType")Integer collectType,@Param("type")Integer type,@Param("search")String search);

}