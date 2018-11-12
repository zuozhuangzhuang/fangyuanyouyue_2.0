package com.fangyuanyouyue.user.dao;

import com.fangyuanyouyue.user.model.MinimsgFormid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MinimsgFormidMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MinimsgFormid record);

    int insertSelective(MinimsgFormid record);

    MinimsgFormid selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MinimsgFormid record);

    int updateByPrimaryKey(MinimsgFormid record);

    /**
     * 获取用户最初的一条formId
     * @param userId
     * @return
     */
    MinimsgFormid getFormId(@Param("userId") Integer userId);
}