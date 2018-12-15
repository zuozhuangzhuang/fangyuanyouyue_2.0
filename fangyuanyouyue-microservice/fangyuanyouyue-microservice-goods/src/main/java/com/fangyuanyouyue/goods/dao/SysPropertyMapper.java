package com.fangyuanyouyue.goods.dao;

import com.fangyuanyouyue.goods.model.SysProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}