package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.SysProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysPropertyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysProperty record);

    int insertSelective(SysProperty record);

    SysProperty selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysProperty record);

    int updateByPrimaryKeyWithBLOBs(SysProperty record);

    int updateByPrimaryKey(SysProperty record);


    /**
     * 根据规则类型获取规则信息
     * @param ruleKey
     * @return
     */
    SysProperty getRuleByKey(@Param("ruleKey")String ruleKey);

    /**
     * 分页总条数
     * @param ruleKey
     * @param keyword
     * @param startDate
     * @param endDate
     * @return
     */
    Integer countPage(@Param("ruleKey")String ruleKey,@Param("keyword")String keyword,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 分页获取规则列表
     * @param ruleKey
     * @param keyword
     * @param startDate
     * @param endDate
     * @return
     */
    List<SysProperty> getPage(@Param("ruleKey")String ruleKey, @Param("start")Integer start, @Param("limit")Integer limit,
                       @Param("keyword")String keyword, @Param("startDate")String startDate, @Param("endDate")String endDate,
                       @Param("orders")String orders, @Param("ascType")Integer ascType);
}