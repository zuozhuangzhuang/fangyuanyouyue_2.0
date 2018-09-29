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

    /**
     * 后台分页总数
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("type")Integer type,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);
    
    /**
     * 后台分页数据
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @return
     */
    List<ForumInfo> getPage(@Param("type")Integer type,@Param("start") Integer start,@Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);
    

    List<ForumInfo> getPageVideo(@Param("type")Integer type,@Param("start") Integer start,@Param("limit") Integer limi,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);



    /**
     * 获取当日帖子数量
     * @return
     */
    Integer getTodayForumCount();

    /**
     * 获取全部帖子数量
     * @return
     */
    Integer getAllForumCount();

}