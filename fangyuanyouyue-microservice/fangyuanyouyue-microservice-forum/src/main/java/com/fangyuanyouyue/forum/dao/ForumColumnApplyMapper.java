package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.ForumColumn;
import com.fangyuanyouyue.forum.model.ForumColumnApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ForumColumnApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumColumnApply record);

    int insertSelective(ForumColumnApply record);

    ForumColumnApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumColumnApply record);

    int updateByPrimaryKey(ForumColumnApply record);

    /**
     * 获取专栏申请列表
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @return
     */
    List<ForumColumnApply> selectApplyList(@Param("start")Integer start,@Param("limit")Integer limit,@Param("keyword")String keyword,@Param("status")Integer status);
    
    /**
     * 后台分页总数
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);
    
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
    List<ForumColumnApply> getPage(@Param("start") Integer start,@Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders);
    
    
}