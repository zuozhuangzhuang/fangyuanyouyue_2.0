package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.AppVersion;
import com.fangyuanyouyue.user.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppVersionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppVersion record);

    int insertSelective(AppVersion record);

    AppVersion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppVersion record);

    int updateByPrimaryKey(AppVersion record);

    /**
     * 版本更新
     * @return
     */
    AppVersion getVersion();


    /**
     * 分页总条数
     * @param keyword
     * @param status
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
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @return
     */
    List<AppVersion> getPage(@Param("start") Integer start, @Param("limit") Integer limit, @Param("keyword")String keyword, @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("orders")String orders, @Param("ascType")Integer ascType);

}