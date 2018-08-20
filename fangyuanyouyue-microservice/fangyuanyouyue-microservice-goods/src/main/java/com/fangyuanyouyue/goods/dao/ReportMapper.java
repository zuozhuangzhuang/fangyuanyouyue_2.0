package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}