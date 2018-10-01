package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Feedback record);

    int insertSelective(Feedback record);

    Feedback selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Feedback record);

    int updateByPrimaryKeyWithBLOBs(Feedback record);

    int updateByPrimaryKey(Feedback record);

    /**
     * 分页总条数
     * @param keyword
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("keyword")String keyword, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 分页获取
     * @param start
     * @param limit
     * @param keyword
     * @param startDate
     * @param endDate
     * @param orders
     * @param ascType
     * @return
     */
    List<Feedback> getPage(@Param("start") Integer start, @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);
}