package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.DailyStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DailyStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DailyStatistics record);

    int insertSelective(DailyStatistics record);

    DailyStatistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DailyStatistics record);

    int updateByPrimaryKey(DailyStatistics record);

    /**
     * 获取n天内统计记录
     * @param count
     * @return
     */
    List<DailyStatistics> selectByDayCount(@Param("count")Integer count);
}