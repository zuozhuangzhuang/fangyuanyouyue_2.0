package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.Rule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Rule record);

    int insertSelective(Rule record);

    Rule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Rule record);

    int updateByPrimaryKeyWithBLOBs(Rule record);

    int updateByPrimaryKey(Rule record);

    /**
     * 根据规则类型获取规则信息
     * @param ruleType
     * @return
     */
    Rule getRuleByType(@Param("ruleType")Integer ruleType);

    /**
     * 分页总条数
     * @param ruleType
     * @param keyword
     * @param startDate
     * @param endDate
     * @return
     */
    Integer countPage(@Param("ruleType")Integer ruleType,@Param("keyword")String keyword,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 分页获取规则列表
     * @param ruleType
     * @param keyword
     * @param startDate
     * @param endDate
     * @return
     */
    List<Rule> getPage(@Param("ruleType")Integer ruleType,@Param("start")Integer start,@Param("limit")Integer limit,
                       @Param("keyword")String keyword,@Param("startDate")String startDate, @Param("endDate")String endDate,
                       @Param("orders")String orders, @Param("ascType")Integer ascType);
}