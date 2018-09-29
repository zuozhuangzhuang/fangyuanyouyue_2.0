package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.model.Report;

@Mapper
public interface ReportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Report record);

    int insertSelective(Report record);

    Report selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Report record);

    int updateByPrimaryKey(Report record);
    /**
     * 分页总条数
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    Integer countForumPage(@Param("type")Integer type, @Param("keyword")String keyword, @Param("status")Integer status, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 分页获取举报帖子、视频列表
     * @param type
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @param ascType
     * @return
     */
    List<Report> getForumReportPage(@Param("type")Integer type, @Param("start") Integer start,
                                    @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("status")Integer status,
                                    @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);

    /**
     * 分页总条数
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    Integer countAppraisalPage(@Param("keyword")String keyword, @Param("status")Integer status, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 分页获取举报帖子、视频列表
     * @param type
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @param ascType
     * @return
     */
    List<Report> getAppraisalReportPage(@Param("start") Integer start,
                                    @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("status")Integer status,
                                    @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);
}