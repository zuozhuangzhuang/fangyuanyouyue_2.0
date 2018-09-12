package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Report record);

    int insertSelective(Report record);

    Report selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Report record);

    int updateByPrimaryKey(Report record);

    /**
     * 根据用户id和举报对象id、类型获取举报信息
     * @param userId
     * @param businessId
     * @param type
     * @return
     */
    Report selectByUserIdBusinessId(@Param("userId")Integer userId,@Param("businessId")Integer businessId,@Param("type")Integer type);

    /**
     * 分页总条数
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    Integer countPage(@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 分页获取举报商品、抢购列表
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
    List<Report> getReportPage(@Param("type")Integer type,@Param("start") Integer start,
                              @Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,
                              @Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders,@Param("ascType")Integer ascType);
}