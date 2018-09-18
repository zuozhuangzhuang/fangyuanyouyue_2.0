package com.fangyuanyouyue.forum.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fangyuanyouyue.forum.dto.ForumColumnDto;
import com.fangyuanyouyue.forum.model.ForumColumn;
@Mapper
public interface ForumColumnMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(ForumColumn record);

    int insertSelective(ForumColumn record);

    ForumColumn selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumColumn record);

    int updateByPrimaryKey(ForumColumn record);

    List<ForumColumn> selectPage(@Param("typeId")Integer typeId,@Param("start")Integer start,@Param("limit")Integer limit);

    List<ForumColumn> selectChosen(@Param("start")Integer start,@Param("limit")Integer limit,@Param("isChosen")Integer isChosen);


    /**
     * 根据用户id获取专栏，每个用户只能有一个专栏
     * @param userId
     * @return
     */
    ForumColumn selectByUserId(@Param("userId")Integer userId);

    /**
     * 根据名称获取专栏
     * @param name
     * @return
     */
    ForumColumn selectByName(@Param("name")String name);
    
    /**
     * 后台分页总数
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    int countPage(@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate);
    
    /**
     * 后台分页数据
     * @param start
     * @param limit
     * @param keyword
     * @param status
     * @param startDate
     * @param endDate
     * @param orders
     * @return
     */
    List<ForumColumn> getPage(@Param("start") Integer start,@Param("limit") Integer limit,@Param("keyword")String keyword,@Param("status")Integer status,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("orders")String orders);


}