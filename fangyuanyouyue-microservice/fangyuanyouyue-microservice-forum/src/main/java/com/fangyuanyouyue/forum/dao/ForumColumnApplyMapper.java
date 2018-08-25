package com.fangyuanyouyue.forum.dao;

import com.fangyuanyouyue.forum.model.ForumColumnApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ForumColumnApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumColumnApply record);

    int insertSelective(ForumColumnApply record);

    ForumColumnApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumColumnApply record);

    int updateByPrimaryKey(ForumColumnApply record);

    /**
     * 获取专栏申请列表
     * @param start
     * @param limit
     * @param search
     * @return
     */
    List<ForumColumnApply> selectApplyList(@Param("start")Integer start,@Param("limit")Integer limit,@Param("keyword")String keyword);
}